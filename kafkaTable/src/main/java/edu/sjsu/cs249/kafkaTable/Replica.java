package edu.sjsu.cs249.kafkaTable;

import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Replica {
    public String kafkaServer;
    public String replicaName;
    public Integer messagesToTakeTheSnapshot;
    public String topicPrefix;
    public String groupId;
    public Map<String, Integer> state;
    public Long operationsOffset;
    public Map<String, Integer> clientCounter;
    public Long snapshotOffset;
    public Map<IncRequest, StreamObserver<IncResponse>> pendingIncRequests;
    public Map<GetRequest, StreamObserver<GetResponse>> pendingGetRequest;
    public KafkaConsumer SnapshotOrderingConsumer;
    public ReentrantLock lock;


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
        this.pendingIncRequests = new HashMap<IncRequest, StreamObserver<IncResponse>>();
        this.pendingGetRequest = new HashMap<GetRequest, StreamObserver<GetResponse>>();
        this.lock = lock;

        var properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        this.SnapshotOrderingConsumer = new KafkaConsumer<>(properties, new StringDeserializer(), new ByteArrayDeserializer());
        this.SnapshotOrderingConsumer.subscribe(List.of(this.topicPrefix + "snapshotOrdering"));
    }

    public void publishInSnapshotOrdering() {
        var properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);

        //check if I am already there in the snapshot ordering topic
        Boolean alreadyPublished = Boolean.FALSE;
        try{
            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var consumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            consumer.subscribe(List.of(this.topicPrefix + "snapshotOrdering"));
            var records = consumer.poll(Duration.ofMillis(1));
            for(var record: records){
                var message = SnapshotOrdering.parseFrom(record.value());
                if(message.getReplicaId() == this.replicaName){
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

        //consume if a snapshot is there in the snapshot topic
        try{
            StringDeserializer deserializer = new StringDeserializer();
            deserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            ByteArrayDeserializer arrayDeserializer = new ByteArrayDeserializer();
            arrayDeserializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
            var snapshotConsumer = new KafkaConsumer<>(properties, deserializer, arrayDeserializer);
            snapshotConsumer.subscribe(List.of(this.topicPrefix + "snapshot"));
            var snapshotRecords = snapshotConsumer.poll(Duration.ofMillis(1));
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
                this.state = latestSnapshot.getTable();
                this.operationsOffset = latestSnapshot.getOperationsOffset();
                this.clientCounter = latestSnapshot.getClientCounters();
                //Update the snapshot offset
                this.snapshotOffset = latestSnapshot.getSnapshotOrderingOffset();
            }
            else{
                System.out.println("No snapshot is there in snapshot topics.");
            }

            snapshotConsumer.unsubscribe();
            snapshotConsumer.close();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void tryToPublishSnapshot() {
        String replicaToTakeTheSnapshot = null;

        //determining which replica will take the snapshot
        try{
            var records = this.SnapshotOrderingConsumer.poll(Duration.ofMillis(1));
            if(records.count() == 0){
                System.out.println("No replica is there in snapshot ordering topic.");
            }
            if(records.count() > 1){
                System.out.println("I have consumed more than one record in snapshot ordering topic. " +
                        "An error may be cause because of this in the future.");
            }
            for(var record: records){
                System.out.println(record);
            }

        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void subscribeToOperationsTopic() {
        try{

            var properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaServer);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);

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
                    if((this.snapshotOffset)%(this.messagesToTakeTheSnapshot) == 0){
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
