package edu.sjsu.cs249.kafkaTable;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

public class ServerCLI extends Thread{
    public String kafkaServer;
    public String replicaName;
    public Integer grpcPort;
    public Integer messagesToTakeTheSnapshot;
    public String topicPrefix;
    public Replica replica;
    public ReentrantLock lock;


    ServerCLI(String kafkaServer, String replicaName, Integer grpcPort, Integer messagesToTakeTheSnapshot,
              String topicPrefix, Replica replica, ReentrantLock lock) {
        this.kafkaServer = kafkaServer;
        this.replicaName = replicaName;
        this.grpcPort = grpcPort;
        this.messagesToTakeTheSnapshot = messagesToTakeTheSnapshot;
        this.topicPrefix = topicPrefix;
        this.replica = replica;
        this.lock = lock;
    }

    @Override
    public void run(){
        try{
            Server server = ServerBuilder.forPort(this.grpcPort)
                    .addService(new KafkaTableImpl(this.replica, this.lock))
                    .addService(new KafkaTableDebugImpl(this.replica, this.lock))
                    .build();
            System.out.println("Started the server at port: " + this.grpcPort);
            server.start();
            server.awaitTermination();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
