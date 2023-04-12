package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.util.concurrent.Callable;

public class Main {
    @Command(description = "first version of chain replication")
    public static class StartServer implements Callable<Integer> {
        @Parameters(index = "0", description = "name")
        private String name;

        @Parameters(index = "1", description = "host port of grpc")
        private String grpcHostPort;

        @Parameters(index = "2", description = "list of zookeeper servers")
        private String zkHostPorts;

        @Parameters(index = "3", description = "control path")
        private String controlPath;


        @Override
        public Integer call() throws Exception {
            ChainNode node = new ChainNode(name, grpcHostPort, zkHostPorts, controlPath);
            node.createChainNode();
            node.findPredecessor();
            node.findSuccessor();

            ServerCLI server = new ServerCLI(name, grpcHostPort, zkHostPorts, controlPath, node);
            server.start();

            server.join();

            return 0;
        }
    };
    public static void main(String[] args) {

        System.exit(new CommandLine(new StartServer()).execute(args));
    }
}