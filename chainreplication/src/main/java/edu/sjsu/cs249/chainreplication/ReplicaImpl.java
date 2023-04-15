package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.zookeeper.data.Stat;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class ReplicaImpl extends ReplicaGrpc.ReplicaImplBase {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;

    ReplicaImpl(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
    }
    @Override
    public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        String key = request.getKey();
        int value = request.getNewValue();
        int XId = request.getXid();
        this.node.updateTable(key, value);
        this.node.updateLastXidSeen(XId);
        if(this.node.amITail == Boolean.FALSE){
            try{
                Stat successorNode = this.node.zk.exists(this.controlPath + "/" + this.node.successorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.successorNode, true, successorNode);
                String nodeData = nodeDataBytes.toString();
                int newLineIndex = nodeData.indexOf("\n");
                String succHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = succHostPort.lastIndexOf(':');
                var host = succHostPort.substring(0, lastColon);
                var port = Integer.parseInt(succHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

                UpdateRequest updateRequest = UpdateRequest.newBuilder().setKey(key).setNewValue(this.node.nodeState.get(key)).setXid(XId).build();
                this.node.sentMessages.add(updateRequest);
                UpdateResponse response = stub.update(updateRequest);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        else{
            this.node.updateLastXidAck(XId);
            try{
                Stat predecessorNode = this.node.zk.exists(this.controlPath + "/" + this.node.predecessorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.predecessorNode, true, predecessorNode);
                String nodeData = nodeDataBytes.toString();
                int newLineIndex = nodeData.indexOf("\n");
                String predHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = predHostPort.lastIndexOf(':');
                var host = predHostPort.substring(0, lastColon);
                var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

                AckResponse response = stub.ack(AckRequest.newBuilder().setXid(XId).build());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        UpdateResponse response = UpdateResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

    @Override
    public void newSuccessor(NewSuccessorRequest request, StreamObserver<NewSuccessorResponse> responseObserver) {
        Long succLastZxidSeen = request.getLastZxidSeen();
        if(succLastZxidSeen <= this.node.lastZxIdSeen){
            //I do not need to refresh my view, and you are not my successor
            NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(-1).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else{
            this.node.findSuccessor();
            if(this.node.successorNode != request.getZnodeName()){
                //You are not my successor after I refreshed my view.
                NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(-1).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
                int succLastAck = request.getLastAck();
                this.node.updateLastXidAck(succLastAck);
                if(succLastAck > this.node.lastXidAck){
                    if(this.node.amIHead == Boolean.TRUE){
                        for(int i = 0; i<this.node.sentMessages.size(); i++){
                            if(this.node.sentMessages.get(i).getXid() <= succLastAck){
                                UpdateRequest updateRequest = this.node.sentMessages.get(i);
                                HeadResponse response = HeadResponse.newBuilder().setRc(0).build();
                                this.node.incReqToClient.get(updateRequest).onNext(response);
                                this.node.incReqToClient.get(updateRequest).onCompleted();
                                this.node.incReqToClient.remove(updateRequest);
                            }
                        }
                    }
                    else{
                        try{
                            Stat predecessorNode = this.node.zk.exists(this.controlPath + "/" + this.node.predecessorNode, true);
                            byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.predecessorNode, true, predecessorNode);
                            String nodeData = nodeDataBytes.toString();
                            int newLineIndex = nodeData.indexOf("\n");
                            String predHostPort = nodeData.substring(0, newLineIndex);

                            var lastColon = predHostPort.lastIndexOf(':');
                            var host = predHostPort.substring(0, lastColon);
                            var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                            var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                            var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                            for(int i = 0; i<this.node.sentMessages.size(); i++){
                                if(this.node.sentMessages.get(i).getXid() <= succLastAck){
                                    AckResponse response = stub.ack(AckRequest.newBuilder().setXid(this.node.sentMessages.get(i).getXid()).build());
                                }
                            }

                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }

                    }
                    for(int i = 0; i<this.node.sentMessages.size(); i++){
                        if(this.node.sentMessages.get(i).getXid() <= succLastAck){
                            this.node.sentMessages.remove(i);
                            i--;
                        }
                    }
                }

                if(request.getLastXid() == -1){
                    NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(0).putAllState(this.node.nodeState)
                            .addAllSent(this.node.sentMessages).setLastXid(this.node.lastXIdSeen).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                else{
                    List<UpdateRequest> messagesToBeSent = new ArrayList<UpdateRequest>();
                    for(int i = 0; i<this.node.sentMessages.size(); i++){
                        if(this.node.sentMessages.get(i).getXid() > request.getLastXid()){
                            messagesToBeSent.add(this.node.sentMessages.get(i));
                        }
                    }
                    NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(1).addAllSent(messagesToBeSent)
                            .setLastXid(this.node.lastXIdSeen).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();

                }

            }
        }

    }

    @Override
    public void ack(AckRequest request, StreamObserver<AckResponse> responseObserver) {
        int XId = request.getXid();
        this.node.updateLastXidAck(XId);
        UpdateRequest updateRequest = null;
        for(int i = 0; i<this.node.sentMessages.size(); i++){
            if(this.node.sentMessages.get(i).getXid() == XId){
                updateRequest = this.node.sentMessages.get(i);
                this.node.sentMessages.remove(i);
                break;
            }
        }
        if(this.node.amIHead == Boolean.TRUE){
            if(updateRequest != null && this.node.incReqToClient.containsKey(updateRequest)){
                HeadResponse response = HeadResponse.newBuilder().setRc(0).build();
                this.node.incReqToClient.get(updateRequest).onNext(response);
                this.node.incReqToClient.get(updateRequest).onCompleted();
                this.node.incReqToClient.remove(updateRequest);
            }
        }
        else{
            try{
                Stat predecessorNode = this.node.zk.exists(this.controlPath + "/" + this.node.predecessorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.predecessorNode, true, predecessorNode);
                String nodeData = nodeDataBytes.toString();
                int newLineIndex = nodeData.indexOf("\n");
                String predHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = predHostPort.lastIndexOf(':');
                var host = predHostPort.substring(0, lastColon);
                var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

                AckResponse response = stub.ack(AckRequest.newBuilder().setXid(XId).build());
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }
        }
        AckResponse response = AckResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
