package edu.sjsu.cs249.kafkaTable;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class KafkaTableImpl extends KafkaTableGrpc.KafkaTableImplBase {
    public Replica replica;
    public ReentrantLock lock;

    KafkaTableImpl(Replica replica, ReentrantLock lock){
        this.replica = replica;
        this.lock = lock;
    }

    @Override
    public void inc(IncRequest request, StreamObserver<IncResponse> responseObserver) {
        this.lock.lock();
        System.out.println("I received the inc request.");
        String key = request.getKey();
        int value = request.getIncValue();
        String clientId = request.getXid().getClientid();
        int counter = request.getXid().getCounter();

        //If I have already seen this request
        if(this.replica.clientCounter.containsKey(clientId) && (this.replica.clientCounter.get(clientId) >= counter)){
            //Sending the response back to the client
            this.lock.unlock();
            IncResponse response = IncResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        //I have not seen the request.
        else{
            //Updating the client counter table.
            this.replica.clientCounter.put(clientId, counter);
            //Updating the state table.
            Boolean isUpdated = this.replica.updateTable(key, value);
            //the table is updated.
            if(isUpdated == Boolean.TRUE){
                this.replica.pendingIncRequests.put(request, responseObserver);
                var properties = new Properties();
                properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.replica.kafkaServer);
                try{
                    StringSerializer serializer = new StringSerializer();
                    serializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                    ByteArraySerializer arraySerializer = new ByteArraySerializer();
                    arraySerializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                    var producer = new KafkaProducer<>(properties, serializer, arraySerializer);
                    var bytes = PublishedItem.newBuilder().setInc(request).build().toByteArray();
                    var record = new ProducerRecord<String, byte[]>(this.replica.topicPrefix + "operations", bytes);
                    producer.send(record);
                }
                catch (Exception e){
                    System.out.println("Error in publishing inc request.");
                    System.out.println(e.getMessage());
                }
            }
            //the table is not updated.
            else{
                System.out.println("Not able to update the key as the resulting value would be less than zero.");
            }
            this.lock.unlock();
        }


    }

    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        this.lock.lock();
        String key = request.getKey();
        String clientId = request.getXid().getClientid();
        int counter = request.getXid().getCounter();

        //If I have already seen this request
        if(this.replica.clientCounter.containsKey(clientId) && this.replica.clientCounter.get(clientId) > counter) {
            this.lock.unlock();
            GetResponse response = GetResponse.newBuilder().setValue(this.replica.state.get(key)).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        //I have not seen this request
        else{
            //Updating the client counter table
            this.replica.clientCounter.put(clientId, counter);
            //Updating the pending get requests
            this.replica.pendingGetRequest.put(request, responseObserver);

            var properties = new Properties();
            properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, this.replica.kafkaServer);
            try{
                StringSerializer serializer = new StringSerializer();
                serializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                ByteArraySerializer arraySerializer = new ByteArraySerializer();
                arraySerializer.configure(Collections.singletonMap("charset", "UTF-8"), false);
                var producer = new KafkaProducer<>(properties, serializer, arraySerializer);
                var bytes = PublishedItem.newBuilder().setGet(request).build().toByteArray();
                var record = new ProducerRecord<String, byte[]>(this.replica.topicPrefix + "operations", bytes);
                producer.send(record);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
            this.lock.unlock();
        }
    }
}
