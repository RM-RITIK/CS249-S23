package edu.sjsu.cs249.here;

import edu.sjsu.cs249.iamhere.Grpc;
import edu.sjsu.cs249.iamhere.HereServiceGrpc;
import io.grpc.ManagedChannelBuilder;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;
import java.io.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    @Command(subcommands = { ClientCli.class})
    static class Cli {}
    @Command(name = "client", mixinStandardHelpOptions = true, description = "register attendance for class.")
    static class ClientCli implements Callable<Integer> {
        @Parameters(index = "0", description = "host:port to connect to.")
        String serverPort;

        @Override
        public Integer call() throws Exception {
            System.out.printf("will contact %s\n", serverPort);
            var lastColon = serverPort.lastIndexOf(':');
            var host = serverPort.substring(0, lastColon);
            var port = Integer.parseInt(serverPort.substring(lastColon+1));
            var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
            var stub = HereServiceGrpc.newBlockingStub(channel);
            String str = "";
            try {
                str = new String(
                        Files.readAllBytes(Paths.get("src/main/java/edu/sjsu/cs249/here/Main.java")));
            }

            catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(stub.hello(Grpc.HelloRequest.newBuilder().setName("Ritik Mehta").build()).getMessage());
            System.out.println(stub.here(Grpc.HereRequest.newBuilder().setName("Ritik Mehta").setId(16079128).setCode(str).build()).getMessage());
            return 0;
        }
    }
    public static void main(String[] args) {
        System.exit(new CommandLine(new Cli()).execute(args));
    }
}