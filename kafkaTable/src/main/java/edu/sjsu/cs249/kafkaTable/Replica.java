package edu.sjsu.cs249.kafkaTable;

import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.checkerframework.checker.units.qual.C;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Replica {
    public String kafkaServer;
    public String replicaName;
    public Integer messagesToTakeTheSnapshot;
    public String topicPrefix;
    public Integer noOfOperationMessages;
    public String groupId;
    public Map<String, Integer> state;
    public Long operationsOffset;
    public Map<String, Integer> clientCounter;
    public Long snapshotOffset;
    public Map<IncRequest, StreamObserver<IncResponse>> pendingIncRequests;
    public Map<GetRequest, StreamObserver<GetResponse>> pendingGetRequest;
    public ReentrantLock lock;
    public Integer groupIdCounter;


    Replica(String kafkaServer, String replicaName, Integer messagesToTakeTheSnapshot, String topicPrefix, ReentrantLock lock){
        this.kafkaServer = kafkaServer;
        this.replicaName = replicaName;
        this.messagesToTakeTheSnapshot = messagesToTakeTheSnapshot;
        this.topicPrefix = topicPrefix;
        this.groupId = replicaName + "'s group";
        this.state = new HashMap<String, Integer>();
        this.operationsOffset = -1L;
        this.clientCounter = new HashMap<String, Integer>();
        this.snapshotOffset = -1L;
        this.noOfOperationMessages = 0;
        this.pendingIncRequests = new HashMap<IncRequest, StreamObserver<IncResponse>>();
        this.pendingGetRequest = new HashMap<GetRequest, StreamObserver<GetResponse>>();
        this.lock = lock;
        this.groupIdCounter = 0;
    }

    public void consumeAlreadyExistingSnapshot() {
        System.out.println("consume if a snapshot is there in the snapshot topic");
        this.lock.lock();
        //consume if a snapshot is there in the snapshot topic
        try{
            var properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
            properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");

            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var snapshotConsumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            var sem = new Semaphore(0);
            snapshotConsumer.subscribe(List.of(this.topicPrefix + "snapshot"), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                    System.out.println("A consumer has been unsubscribed");
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    System.out.println("Partition assigned");
                    collection.stream().forEach(t -> snapshotConsumer.seek(t, 0));
                    sem.release();
                }
            });
            snapshotConsumer.poll(0);
            sem.acquire();
            System.out.println("Ready to consume snapshot topic at " + new Date());
            var snapshotRecords = snapshotConsumer.poll(Duration.ofSeconds(5));
            System.out.println("No of records in snapshot topic " + snapshotRecords.count());
            var maxRecord = new ConsumerRecord<String, byte[]>("snapshot", 0, 0, null, null);
            long maxOffset = -1;
            for(var record: snapshotRecords){
                if(record.offset() > maxOffset){
                    maxOffset = record.offset();
                    maxRecord = record;
                }
            }
            if(maxOffset != -1){
                Snapshot latestSnapshot = Snapshot.parseFrom(maxRecord.value());
                System.out.println(latestSnapshot);
                this.state = new HashMap<>(latestSnapshot.getTable());
                this.operationsOffset = latestSnapshot.getOperationsOffset();
                this.clientCounter = new HashMap<>(latestSnapshot.getClientCounters());
                //Update the snapshot offset
                this.snapshotOffset = latestSnapshot.getSnapshotOrderingOffset();
            }
            else{
                System.out.println("No snapshot is there in snapshot topics.");
            }

            snapshotConsumer.unsubscribe();
            snapshotConsumer.close();
            this.lock.unlock();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            this.lock.unlock();
        }
    }

    public void publishInSnapshotOrdering() {
        System.out.println("publish in snapshot ordering");
        var properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId + Integer.toString(this.groupIdCounter));
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        this.groupIdCounter = this.groupIdCounter + 1;

        //check if I am already there in the snapshot ordering topic
        Boolean alreadyPublished = Boolean.FALSE;
        try{
            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var consumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            var sem = new Semaphore(0);
            consumer.subscribe(List.of(this.topicPrefix + "snapshotOrdering"), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                    System.out.println("A consumer has been unsubscribed");
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    System.out.println("Partition assigned");
                    collection.stream().forEach(t -> consumer.seek(t, snapshotOffset.intValue() + 1));
                    sem.release();
                }
            });
            consumer.poll(0);
            sem.acquire();
            System.out.println("Ready to consume snapshot ordering topic at " + new Date());
            var records = consumer.poll(Duration.ofSeconds(5));
            System.out.println("No of records in snapshot ordering topic " + records.count());
            System.out.println(this.snapshotOffset);
            for(var record: records){
                System.out.println(record.offset());
                var message = SnapshotOrdering.parseFrom(record.value());
                if(message.getReplicaId().equals(this.replicaName)){
                    alreadyPublished = Boolean.TRUE;
                }
            }

            consumer.unsubscribe();
            consumer.close();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        //if not published, publish it
        if(alreadyPublished == Boolean.FALSE){
            StringSerializer serializer = new StringSerializer();
            serializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArraySerializer arraySerializer = new ByteArraySerializer();
            arraySerializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var producer = new KafkaProducer<>(properties, serializer, arraySerializer);

            try{
                var bytes = SnapshotOrdering.newBuilder()
                        .setReplicaId(this.replicaName)
                        .build().toByteArray();
                var record = new ProducerRecord<String, byte[]>(this.topicPrefix + "snapshotOrdering", bytes);
                producer.send(record);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
        else{
            System.out.println("I am already published in the snapshot ordering ");
        }
    }

    public void tryToPublishSnapshot() {
        this.lock.lock();
        var properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId + Integer.toString(this.groupIdCounter));
        properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        this.groupIdCounter = this.groupIdCounter + 1;

        String replicaToTakeTheSnapshot = "";
        System.out.println("The person in the front at the snapshot ordering queue will now take the snapshot.");

        //determining which replica will take the snapshot
        try{
            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var consumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            var sem = new Semaphore(0);
            consumer.subscribe(List.of(this.topicPrefix + "snapshotOrdering"), new ConsumerRebalanceListener() {
                @Override
                public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                    System.out.println("A consumer has been unsubscribed");
                }

                @Override
                public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                    System.out.println("Partition assigned");
                    collection.stream().forEach(t -> consumer.seek(t, snapshotOffset.intValue() + 1));
                    sem.release();
                }
            });
            consumer.poll(0);
            sem.acquire();
            System.out.println("Ready to consume snapshot ordering topic at " + new Date());
            var records = consumer.poll(Duration.ofSeconds(5));
            System.out.println("No of records in snapshot ordering topic " + records.count());
            if(records.count() > 1){
                System.out.println("I have consumed more than one record in snapshot ordering topic. " +
                        "An error may be cause because of this in the future.");
            }
            for(var record: records){
                this.snapshotOffset = record.offset();
                replicaToTakeTheSnapshot = SnapshotOrdering.parseFrom(record.value()).getReplicaId();
            }
            System.out.println("It's " + replicaToTakeTheSnapshot + "'s turn to take the snapshot.");
            //I will take the snapshot
            if(replicaToTakeTheSnapshot != "" && replicaToTakeTheSnapshot.equals(this.replicaName)){
                System.out.println("I will take the snapshot");
                try {
                    StringSerializer serializer = new StringSerializer();
                    serializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                    ByteArraySerializer arraySerializer = new ByteArraySerializer();
                    arraySerializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                    var producer = new KafkaProducer<>(properties, serializer, arraySerializer);
                    var bytes = Snapshot.newBuilder().setReplicaId(this.replicaName)
                            .putAllTable(this.state)
                            .putAllClientCounters(this.clientCounter)
                            .setSnapshotOrderingOffset(this.snapshotOffset).
                            setOperationsOffset(this.operationsOffset).build().toByteArray();
                    var record = new ProducerRecord<String, byte[]>(this.topicPrefix + "snapshot", bytes);
                    producer.send(record);
                    //publish again in snapshot ordering topic.
                    this.publishInSnapshotOrdering();
                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }



        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
        this.lock.unlock();
    }

    public void subscribeToOperationsTopic() {
        try{

            var properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
            properties.setProperty(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");

            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var consumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            consumer.subscribe(List.of(this.topicPrefix + "operations"));
            while(true){
                var records = consumer.poll(Duration.ofSeconds(1));
                for (var record: records) {
                    //Updating the offset.
                    if(record.offset() > this.operationsOffset){
                        this.operationsOffset = record.offset();
                        this.noOfOperationMessages = this.noOfOperationMessages + 1;
                    }
                    PublishedItem request = PublishedItem.parseFrom(record.value());
                    //If it is a increment request
                    if(request.hasInc() == Boolean.TRUE){
                        //If I had received the original request
                        if(this.pendingIncRequests.containsKey(request.getInc())){
                            System.out.println("Sending the response back to the client.");
                            IncResponse response = IncResponse.newBuilder().build();
                            this.pendingIncRequests.get(request.getInc()).onNext(response);
                            this.pendingIncRequests.get(request.getInc()).onCompleted();
                            this.pendingIncRequests.remove(request.getInc());
                        }
                        //I have not produced the original request. So, I need to check if I need to
                        //update my values.
                        else{
                            //I need to update the value
                            if(this.clientCounter.containsKey(request.getInc().getXid().getClientid()) == Boolean.FALSE ||
                                    (request.getInc().getXid().getCounter() > this.clientCounter.get(request.getInc().getXid().getClientid()))){
                                String key = request.getInc().getKey();
                                int value = request.getInc().getIncValue();
                                Boolean isUpdated = this.updateTable(key, value);
                                //Update the client counter table
                                this.clientCounter.put(request.getInc().getXid().getClientid(), request.getInc().getXid().getCounter());
                                if(isUpdated == Boolean.FALSE){
                                    System.out.println("The table cannot be updated as the value would become less than 0");
                                }
                            }
                        }
                    }
                    //If it is a get request
                    else{
                        //If I sent the original get request
                        if(this.pendingGetRequest.containsKey(request.getGet())){
                            GetResponse response = GetResponse.newBuilder().
                                    setValue(this.state.containsKey(request.getGet().getKey())?
                                            this.state.get(request.getGet().getKey())
                                            : 0).
                                    build();
                            this.pendingGetRequest.get(request.getGet()).onNext(response);
                            this.pendingGetRequest.get(request.getGet()).onCompleted();
                            this.pendingGetRequest.remove(request.getGet());
                        }
                        //I did not send the original get request
                        else{
                            if(this.clientCounter.containsKey(request.getGet().getXid().getClientid()) == Boolean.FALSE ||
                                    (request.getGet().getXid().getCounter() > this.clientCounter.get(request.getGet().getXid().getClientid()))){
                                this.clientCounter.put(request.getGet().getXid().getClientid(), request.getGet().getXid().getCounter());
                            }
                        }

                    }
                    if(this.noOfOperationMessages != 0 && (this.noOfOperationMessages)%(this.messagesToTakeTheSnapshot) == 0){
                        System.out.println("time to take the snapshot");
                        this.tryToPublishSnapshot();
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public Boolean updateTable(String key, int value) {
        int prev_value = 0;
        if(this.state.containsKey(key)){
            prev_value = this.state.get(key);
        }
        int new_value = value + prev_value;
        if(new_value >= 0){
            this.state.put(key, new_value);
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }



}