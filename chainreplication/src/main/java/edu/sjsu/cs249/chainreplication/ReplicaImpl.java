package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ReplicaImpl extends ReplicaGrpc.ReplicaImplBase {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;
    public ReentrantLock lock;

    ReplicaImpl(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node, ReentrantLock lock){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
        this.lock = lock;
    }

    class sendAcknowledgementRequest implements Runnable {
        private ChainNode node;
        public String controlPath;
        int XId;
        sendAcknowledgementRequest(ChainNode node, String controlPath, int XId){
            this.node = node;
            this.controlPath = controlPath;
            this.XId = XId;
        }

        @Override
        public void run() {
            try{
                Stat predecessorNode = this.node.zk.exists(this.controlPath + "/" + this.node.predecessorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.predecessorNode, true, predecessorNode);
                String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                int newLineIndex = nodeData.indexOf("\n");
                String predHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = predHostPort.lastIndexOf(':');
                var host = predHostPort.substring(0, lastColon);
                var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                AckRequest ackRequest = AckRequest.newBuilder().setXid(this.XId).build();
                AckResponse response = stub.ack(ackRequest);
            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    class sendUpdateRequestToSuccessor implements Runnable {
        private ChainNode node;
        public String controlPath;
        public String key;
        public int XId;
        public int value;

        sendUpdateRequestToSuccessor(ChainNode node, String controlPath, String key, int XId, int value){
            this.node = node;
            this.controlPath = controlPath;
            this.key = key;
            this.XId = XId;
            this.value = value;
        }

        @Override
        public void run() {
            try{
                Stat successorNode = this.node.zk.exists(this.controlPath + "/" + this.node.successorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.successorNode, true, successorNode);
                String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                int newLineIndex = nodeData.indexOf("\n");
                String succHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = succHostPort.lastIndexOf(':');
                var host = succHostPort.substring(0, lastColon);
                var port = Integer.parseInt(succHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

                UpdateRequest updateRequest = UpdateRequest.newBuilder().setKey(this.key).setNewValue(this.value).setXid(this.XId).build();
                this.node.sentMessages.add(updateRequest);
                UpdateResponse response = stub.update(updateRequest);

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    @Override
    public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        UpdateResponse response = UpdateResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
        this.lock.lock();
        System.out.println("I got the update request: " + request.getXid());
        String key = request.getKey();
        int value = request.getNewValue();
        int XId = request.getXid();
        this.node.nodeState.put(key, value);
        this.node.updateLastXidSeen(XId);
        if(this.node.amITail == Boolean.FALSE){
            sendUpdateRequestToSuccessor updateRequest = new sendUpdateRequestToSuccessor(this.node, this.controlPath, key, XId, value);
            FutureTask<String> updateTask = new FutureTask<>(updateRequest, "sent the update request to my successor");

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(updateTask);
        }
        else{
            System.out.println("I sent the ack request: " + request.getXid());
            this.node.updateLastXidAck(XId);
            sendAcknowledgementRequest ackRequest = new sendAcknowledgementRequest(this.node, this.controlPath, XId);
            FutureTask<String> ackTask = new FutureTask<>(ackRequest, "sent the ack request.");

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(ackTask);


        }
        this.lock.unlock();

    }

    class sendAllPendingAckRequests implements Runnable{
        private ChainNode node;
        public String controlPath;
        public int succLastAck;
        sendAllPendingAckRequests(ChainNode node, String controlPath, int succLastAck){
            this.node = node;
            this.controlPath = controlPath;
            this.succLastAck = succLastAck;
        }

        @Override
        public void run() {
            try{
                Stat predecessorNode = this.node.zk.exists(this.controlPath + "/" + this.node.predecessorNode, true);
                byte[] nodeDataBytes = this.node.zk.getData(this.controlPath + "/" + this.node.predecessorNode, true, predecessorNode);
                String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                int newLineIndex = nodeData.indexOf("\n");
                String predHostPort = nodeData.substring(0, newLineIndex);

                var lastColon = predHostPort.lastIndexOf(':');
                var host = predHostPort.substring(0, lastColon);
                var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                var stub = ReplicaGrpc.newBlockingStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                for(int i = 0; i<this.node.sentMessages.size(); i++){
                    if(this.node.sentMessages.get(i).getXid() <= this.succLastAck){
                        AckRequest request = AckRequest.newBuilder().setXid(this.node.sentMessages.get(i).getXid()).build();
                        AckResponse response = stub.ack(request);
                    }
                }

            }
            catch (Exception e){
                System.out.println(e.getMessage());
            }

        }
    }

    @Override
    public void newSuccessor(NewSuccessorRequest request, StreamObserver<NewSuccessorResponse> responseObserver) {
        this.lock.lock();
        long succLastZxidSeen = request.getLastZxidSeen();
        if(succLastZxidSeen < this.node.lastZxIdSeen){
            System.out.println("I do not need to refresh my view, and you are not my successor");
            this.lock.unlock();
            NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(-1).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else{
            this.node.findSuccessor();
            if(this.node.successorNode.equals(request.getZnodeName()) == false){
                System.out.println("You are not my successor after I refreshed my view.");
                this.lock.unlock();
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
                        sendAllPendingAckRequests ackPendingRequests = new sendAllPendingAckRequests(this.node, this.controlPath, succLastAck);
                        FutureTask<String> ackPendingTask = new FutureTask<String>(ackPendingRequests, "sent ack to all pending requests");

                        ExecutorService executor = Executors.newFixedThreadPool(1);
                        executor.submit(ackPendingTask);


                    }
                    for(int i = 0; i<this.node.sentMessages.size(); i++){
                        if(this.node.sentMessages.get(i).getXid() <= succLastAck){
                            this.node.sentMessages.remove(i);
                            i--;
                        }
                    }
                }

                if(request.getLastXid() == -1){
                    System.out.println("new node added.");
                    this.lock.unlock();
                    NewSuccessorResponse response = NewSuccessorResponse.newBuilder().setRc(0).putAllState(this.node.nodeState)
                            .addAllSent(this.node.sentMessages).setLastXid(this.node.lastXIdSeen == null ? -1 : this.node.lastXIdSeen).build();
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
                    this.lock.unlock();
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
        this.lock.lock();
        AckResponse ackResponse = AckResponse.newBuilder().build();
        responseObserver.onNext(ackResponse);
        responseObserver.onCompleted();
        System.out.println("I got the ack request: " + request.getXid());
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
            sendAcknowledgementRequest ackRequest = new sendAcknowledgementRequest(this.node, this.controlPath, XId);
            FutureTask<String> ackTask = new FutureTask<String>(ackRequest, "sent the ack request to my predecessor");

            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(ackTask);
            System.out.println("I sent the ack request to my pred: " + ackRequest.XId);
        }
        this.lock.unlock();
    }
}