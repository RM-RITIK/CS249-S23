package edu.sjsu.cs249.abd;

import edu.sjsu.cs249.abd.Grpc;
import edu.sjsu.cs249.abd.ABDServiceGrpc;
import edu.sjsu.cs249.abd.ABDServiceGrpc.ABDServiceImplBase;
import edu.sjsu.cs249.abd.Grpc.NameRequest;
import edu.sjsu.cs249.abd.Grpc.NameResponse;
import edu.sjsu.cs249.abd.Grpc.EnableRequest;
import edu.sjsu.cs249.abd.Grpc.EnableResponse;
import edu.sjsu.cs249.abd.Grpc.Read1Request;
import edu.sjsu.cs249.abd.Grpc.Read1Response;
import edu.sjsu.cs249.abd.Grpc.Read2Request;
import edu.sjsu.cs249.abd.Grpc.Read2Response;
import edu.sjsu.cs249.abd.Grpc.WriteRequest;
import edu.sjsu.cs249.abd.Grpc.WriteResponse;
import edu.sjsu.cs249.abd.Grpc.ExitRequest;
import edu.sjsu.cs249.abd.Grpc.ExitResponse;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.*;


import java.lang.Math;

public class Main {

    public static void main(String[] args) {
        System.exit(new CommandLine(new Cli()).execute(args));
    }

    public static HashMap<String, Long[]> RegisterValues = new HashMap<String, Long[]>();

    public static class ABDServiceImpl extends ABDServiceImplBase {
        public static boolean read1_access = true;
        public static boolean read2_access = true;
        public static boolean write_access = true;

        @Override
        public void name(NameRequest request, StreamObserver<NameResponse> responseObserver){
            NameResponse response = NameResponse.newBuilder().setName("Ritik Mehta").build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void enableRequests(EnableRequest request, StreamObserver<EnableResponse> responseObserver){
            read1_access = request.getRead1();
            read2_access = request.getRead2();
            write_access = request.getWrite();

            EnableResponse response = EnableResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void read1(Read1Request request, StreamObserver<Read1Response> responseObserver){
            if(read1_access == false){
                System.out.println("Read1 is disabled");
                return;
            }
            long register = request.getAddr();
            if(RegisterValues.containsKey(Long.toString(register))){
                Long[] label_value = RegisterValues.get(Long.toString(register));
                long label = label_value[0];
                long value = label_value[1];
                Read1Response response = Read1Response.newBuilder().setRc(0).setLabel(label).setValue(value).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
                Read1Response response = Read1Response.newBuilder().setRc(1).setLabel(-1).setValue(-1).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }

        @Override
        public void read2(Read2Request request, StreamObserver<Read2Response> responseObserver){
            if(read2_access == false){
                System.out.println("Read2 is disabled");
                return;
            }
            String register = Long.toString(request.getAddr());
            long label = request.getLabel();
            long value = request.getValue();
            if(RegisterValues.containsKey(register)){
                Long[] label_value_present = RegisterValues.get(register);

                if(label_value_present[0] < label){
                    Long[] label_value_new = new Long[2];
                    label_value_new[0] = label;
                    label_value_new[1] = value;
                    RegisterValues.put(register, label_value_new);
                }
            }
            else{
                Long[] label_value_new = new Long[2];
                label_value_new[0] = label;
                label_value_new[1] = value;
                RegisterValues.put(register, label_value_new);
            }

            Read2Response response = Read2Response.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public synchronized void write(WriteRequest request, StreamObserver<WriteResponse> responseObserver){
            if(write_access == false){
               System.out.println("Write is disabled.");
               return;
            }
            String register = Long.toString(request.getAddr());
            long label = request.getLabel();
            long value = request.getValue();
            if(RegisterValues.containsKey(register)){
                Long[] label_value_present = RegisterValues.get(register);

                if(label_value_present[0] < label){
                    Long[] label_value_new = new Long[2];
                    label_value_new[0] = label;
                    label_value_new[1] = value;
                    RegisterValues.put(register, label_value_new);
                }
            }
            else{
                Long[] label_value_new = new Long[2];
                label_value_new[0] = label;
                label_value_new[1] = value;
                RegisterValues.put(register, label_value_new);
            }

            WriteResponse response = WriteResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void exit(ExitRequest request, StreamObserver<ExitResponse> responseObserver){
            System.exit(0);
            ExitResponse response = ExitResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
    };

    @Command(subcommands = {ServerCli.class, ClientCli.class})
    static class Cli {}

    @Command(name = "server", mixinStandardHelpOptions = true, description = "start an ABD register server.")
    static class ServerCli implements Callable<Integer> {
        @Parameters(index = "0", description = "host:port listen on.")
        int serverPort;

        @Override
        public Integer call() throws Exception {
            System.out.printf("will contact %s\n", serverPort);
            Server server = ServerBuilder
                    .forPort(serverPort)
                    .addService(new ABDServiceImpl()).build();

            server.start();
            server.awaitTermination();
            return 0;
        }
    }


    @Command(name = "client", mixinStandardHelpOptions = true, description = "start and ADB client.")
    static class ClientCli {
        @Parameters(index = "0", description = "comma separated list of servers to use.")
        String serverPorts;

        @Command
        public void read(@Parameters(paramLabel = "register") String register) throws InterruptedException {
            ArrayList<String> servers = new ArrayList<>(Arrays.asList(serverPorts.split(",")));
            int n = servers.size();
            final int[] num_read_responses = {0};
            HashMap<String, Long[]> readValues = new HashMap<String, Long[]>();
            final CountDownLatch threadCounterRead1 = new CountDownLatch(servers.size()/2 + 1);
            for(int i = 0; i<n; i++){
                var server = servers.get(i);
                var lastColon = servers.get(i).lastIndexOf(':');
                var host = servers.get(i).substring(0, lastColon);
                var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ABDServiceGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                var request = Grpc.Read1Request.newBuilder().setAddr(Long.parseLong(register)).build();

                StreamObserver<Read1Response> responseObserver = new StreamObserver<Read1Response>() {
                    @Override
                    public void onNext(Read1Response response) {
                        long label = response.getLabel();
                        long value = response.getValue();
                        long isValuePresent = response.getRc();
                        if(isValuePresent != 1){
                            Long[] label_value_pair = new Long[2];
                            label_value_pair[0] = label;
                            label_value_pair[1] = value;
                            readValues.put(server, label_value_pair);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        channel.shutdown();

                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdown();
                        num_read_responses[0] = num_read_responses[0] + 1;
                        threadCounterRead1.countDown();

                    }
                };
                stub.read1(request, responseObserver);

            }
            threadCounterRead1.await(3, TimeUnit.SECONDS);

            if (num_read_responses[0] >= Math.ceil((n+1)/2) && readValues.size() > 0){
                long max_label = -1000000;
                long max_value = -1000000;
                for (Map.Entry<String,Long[]> mapElement : readValues.entrySet()) {
                    Long[] label_value_pair = mapElement.getValue();
                    if(label_value_pair[0] > max_label){
                        max_label = label_value_pair[0];
                        max_value = label_value_pair[1];
                    }
                }
                final CountDownLatch threadCounterRead2 = new CountDownLatch(servers.size()/2 + 1);
                final int[] numRead2Success = {0};
                for(int i = 0; i<n; i++){
                    var lastColon = servers.get(i).lastIndexOf(':');
                    var host = servers.get(i).substring(0, lastColon);
                    var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                    var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                    var stub = ABDServiceGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                    var request = Grpc.Read2Request.newBuilder().setAddr(Long.parseLong(register)).setLabel(max_label).setValue(max_value).build();
                    StreamObserver<Read2Response> responseObserver = new StreamObserver<Read2Response>() {
                        @Override
                        public void onNext(Read2Response read2Response) {

                        }

                        @Override
                        public void onError(Throwable throwable) {
                            channel.shutdown();

                        }

                        @Override
                        public void onCompleted() {
                            channel.shutdown();
                            numRead2Success[0] = numRead2Success[0] + 1;
                            threadCounterRead2.countDown();
                        }
                    };
                    stub.read2(request, responseObserver);
                }

                threadCounterRead2.await(3, TimeUnit.SECONDS);

                if(numRead2Success[0] >= Math.ceil((n+1)/2)){
                    System.out.println(max_value + "(" + max_label + ")");
                }
                else{
                    System.out.println("failed");
                }


            }
            else{
                System.out.println("failed");
            }


        }


        @Command
        public void write(@Parameters(paramLabel = "register") String register,
                          @Parameters(paramLabel = "value") String value) throws InterruptedException {
            ArrayList<String> servers = new ArrayList<>(Arrays.asList(serverPorts.split(",")));
            int n = servers.size();
            long label = System.currentTimeMillis();
            final int[] numWriteSuccesses = {0};
            final CountDownLatch threadCounter = new CountDownLatch(servers.size()/2 + 1);

            for(int i = 0; i<n; i++){
                var lastColon = servers.get(i).lastIndexOf(':');
                var host = servers.get(i).substring(0, lastColon);
                var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ABDServiceGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                var request = Grpc.WriteRequest.newBuilder().setAddr(Long.parseLong(register)).setLabel(label).setValue(Long.parseLong(value)).build();
                StreamObserver<WriteResponse> responseObserver = new StreamObserver<WriteResponse>() {
                    @Override
                    public void onNext(WriteResponse response) {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        channel.shutdown();
                    }

                    @Override
                    public void onCompleted() {
                        channel.shutdown();
                        numWriteSuccesses[0] = numWriteSuccesses[0] + 1;
                        threadCounter.countDown();

                    }
                };
                stub.write(request, responseObserver);

            }
            threadCounter.await(3, TimeUnit.SECONDS);

            if(numWriteSuccesses[0] >= Math.ceil((n+1)/2)){
                System.out.println("success");
            }
            else{
                System.out.println("failure");
            }
        }
    }
}
