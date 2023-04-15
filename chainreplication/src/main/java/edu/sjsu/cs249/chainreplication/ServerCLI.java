package edu.sjsu.cs249.chainreplication;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerCLI extends Thread {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;

    ServerCLI(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
    }

    @Override
    public void run() {
        var lastColon = grpcHostPort.lastIndexOf(':');
        var host = grpcHostPort.substring(0, lastColon);
        int serverPort = Integer.parseInt(grpcHostPort.substring(lastColon+1));
        System.out.printf("will contact %s\n", serverPort);
        try{
            Server server = ServerBuilder
                    .forPort(serverPort)
                    .addService(new HeadChainReplicaImpl(this.name, this.grpcHostPort, this.zkHostPorts, this.controlPath, this.node))
                    .addService(new TailChainReplicaImpl(this.name, this.grpcHostPort, this.zkHostPorts, this.controlPath, this.node))
                    .addService(new ReplicaImpl(this.name, this.grpcHostPort, this.zkHostPorts, this.controlPath, this.node))
                    .addService(new ChainDebugImpl(this.node))
                    .build();

            server.start();
            server.awaitTermination();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
