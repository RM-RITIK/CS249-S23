package edu.sjsu.cs249.kafkaTable;

import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.simple.SimpleLogger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

@Command
public class Main {
    @Command(description = "kafka table")
    public static class kafkaTable implements Callable<Integer> {
        static {
            // quiet some kafka messages
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "warn");
        }


        @Parameters(index = "0", description = "replica")
        private String replica;

        @Parameters(index = "1", description = "the host:port of the kafka bootstrap server")
        public String kafkaServer;

        @Parameters(index = "2", description = "the name of the replica")
        public String replicaName;

        @Parameters(index = "3", description = "grpc port")
        public Integer grpcPort;

        @Parameters(index = "4", description = "offset")
        public Integer messagesToTakeTheSnapshot;

        @Parameters(index = "5", description = "topic prefix")
        public String topicPrefix;

        @Override
        public Integer call() throws Exception {
            ReentrantLock lock = new ReentrantLock();
            Replica replica = new Replica(kafkaServer, replicaName, messagesToTakeTheSnapshot, topicPrefix, lock);
            replica.consumeAlreadyExistingSnapshot();
            replica.publishInSnapshotOrdering();

            ServerCLI server = new ServerCLI(kafkaServer, replicaName, grpcPort, messagesToTakeTheSnapshot, topicPrefix, replica, lock);
            server.start();
            replica.subscribeToOperationsTopic();


            server.join();
            return 0;
        }
    }

    public static void main(String[] args) {
        System.exit(new CommandLine(new kafkaTable()).execute(args));
    }
}