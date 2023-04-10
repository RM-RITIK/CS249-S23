package edu.sjsu.cs249.chainreplication;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class ServerCLI extends Thread {
    private String name;
    private String grpcHostPort;
    private String zkHostPorts;
    private String controlPath;

    ServerCLI(String name, String grpcHostPort, String zkHostPorts, String controlPath){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
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
                    .addService(new HeadChainReplicaImpl()).addService(new TailChainReplicaImpl()).
                    addService(new ReplicaImpl()).build();

            server.start();
            server.awaitTermination();

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
