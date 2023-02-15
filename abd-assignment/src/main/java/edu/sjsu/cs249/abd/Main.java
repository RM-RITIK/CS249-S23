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
        public void write(WriteRequest request, StreamObserver<WriteResponse> responseObserver){
            if(write_access == false){
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

        class readFromServer implements Callable
        {
            private String host;
            private int port;
            private String register;

            public readFromServer(String host, int port, String register){
                this.host = host;
                this.port = port;
                this.register = register;
            }

            @Override
            public Long[] call() throws Exception
            {
                var channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
                var stub = ABDServiceGrpc.newBlockingStub(channel);
                var response = stub.read1(Grpc.Read1Request.newBuilder().setAddr(Long.parseLong(this.register)).build());
                long label = response.getLabel();
                long value = response.getValue();
                long isValuePresent = response.getRc();
                return new Long[] {label, value, isValuePresent};
            }

        };

        class announceRead implements Callable
        {
            private String host;
            private int port;
            private long label;
            private long value;
            private String register;

            public announceRead(String host, int port, String register, long label, long value){
                this.host = host;
                this.port = port;
                this.register = register;
                this.label = label;
                this.value = value;
            }

            @Override
            public String call() throws Exception
            {
                var channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
                var stub = ABDServiceGrpc.newBlockingStub(channel);
                try{
                    var response = stub.read2(Grpc.Read2Request.newBuilder().setAddr(Long.parseLong(this.register)).setLabel(this.label).setValue(this.value).build());
                    return "sucess";
                }
                catch (StatusRuntimeException ex){
                    return "failure";
                }
            }

        };

        @Command
        public void read(@Parameters(paramLabel = "register") String register) {
            ArrayList<String> servers = new ArrayList<>(Arrays.asList(serverPorts.split(",")));
            int n = servers.size();
            int num_read_responses = 0;
            HashMap<String, Long[]> readValues = new HashMap<String, Long[]>();
            for(int i = 0; i<n; i++){
                System.out.printf("will read from server %s\n", servers.get(i));
                var lastColon = servers.get(i).lastIndexOf(':');
                var host = servers.get(i).substring(0, lastColon);
                var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                readFromServer readObject = new readFromServer(host, port, register);
                RunnableFuture<Long[]> future = new FutureTask<>(readObject);
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(future);
                Long[] result = null;
                try
                {
                    result = future.get(3, TimeUnit.SECONDS);    // wait 3 seconds
                }
                catch (Exception ex)
                {
                    System.out.println("Cannot read from server " + servers.get(i));
                    future.cancel(true);
                }
                service.shutdown();

                if(result != null){
                    num_read_responses = num_read_responses + 1;
                    if(result[2] != 1){
                        Long[] label_value_pair = new Long[2];
                        label_value_pair[0] = result[0];
                        label_value_pair[1] = result[1];
                        readValues.put(servers.get(i), label_value_pair);
                    }
                }
            }
            System.out.println(num_read_responses);

            if (num_read_responses >= Math.ceil((n+1)/2) && readValues.size() > 0){
                System.out.println("Read1 was successful");
                long max_label = -1000000;
                long max_value = -1000000;
                for (Map.Entry<String,Long[]> mapElement : readValues.entrySet()) {
                    Long[] label_value_pair = mapElement.getValue();
                    if(label_value_pair[0] > max_label){
                        max_label = label_value_pair[0];
                        max_value = label_value_pair[1];
                    }
                }

                int numRead2Success = 0;
                for(int i = 0; i<n; i++){
                    System.out.printf("will announce read value to server %s\n", servers.get(i));
                    var lastColon = servers.get(i).lastIndexOf(':');
                    var host = servers.get(i).substring(0, lastColon);
                    var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                    announceRead announceObj = new announceRead(host, port, register, max_label, max_value);
                    RunnableFuture<String> future = new FutureTask<>(announceObj);
                    ExecutorService service = Executors.newSingleThreadExecutor();
                    service.execute(future);
                    String result = null;
                    try{
                        result = future.get(3, TimeUnit.SECONDS);
                    }
                    catch (Exception ex){
                        System.out.println("Announe read to server " + servers.get(i) + " was not successfull.");
                        future.cancel(true);
                    }
                    service.shutdown();

                    if(result != null && result != "failure"){
                        numRead2Success = numRead2Success + 1;
                    }
                }

                if(numRead2Success >= Math.ceil((n+1)/2)){
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
        class announceWrite implements Callable
        {
            private String host;
            private int port;
            private String register;
            private String value;
            private long label;

            announceWrite(String host, int port, String register, String value, long label){
                this.host = host;
                this.port = port;
                this.register = register;
                this.value = value;
                this.label = label;
            }

            @Override
            public String call() throws Exception
            {
                var channel = ManagedChannelBuilder.forAddress(this.host, this.port).usePlaintext().build();
                var stub = ABDServiceGrpc.newBlockingStub(channel);
                try{
                    var response = stub.write(Grpc.WriteRequest.newBuilder().setAddr(Long.parseLong(this.register)).setLabel(this.label).setValue(Long.parseLong(this.value)).build());
                    return "sucess";
                }
                catch (StatusRuntimeException ex){
                    return "failure";
                }


            }
        };

        @Command
        public void write(@Parameters(paramLabel = "register") String register,
                          @Parameters(paramLabel = "value") String value) {
            ArrayList<String> servers = new ArrayList<>(Arrays.asList(serverPorts.split(",")));
            int n = servers.size();
            long label = System.currentTimeMillis();
            int numWriteSuccesses = 0;

            for(int i = 0; i<n; i++){
                System.out.printf("will communicate new read value to server %s\n", servers.get(i));
                var lastColon = servers.get(i).lastIndexOf(':');
                var host = servers.get(i).substring(0, lastColon);
                var port = Integer.parseInt(servers.get(i).substring(lastColon+1));
                announceWrite writeObj = new announceWrite(host, port, register, value, label);
                RunnableFuture<String> future = new FutureTask<>(writeObj);
                ExecutorService service = Executors.newSingleThreadExecutor();
                service.execute(future);
                String result = null;
                try{
                    result = future.get(3, TimeUnit.SECONDS);
                }
                catch (Exception ex){
                    System.out.println("Communication of new write value to server " + servers.get(i) + " was not successfull.");
                    future.cancel(true);
                }
                service.shutdown();

                if(result != null && result != "failure"){
                    numWriteSuccesses = numWriteSuccesses + 1;
                }
            }

            if(numWriteSuccesses >= Math.ceil((n+1)/2)){
                System.out.println("success");
            }
            else{
                System.out.println("failure");
            }
        }
    }
}
