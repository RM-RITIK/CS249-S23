package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.stub.StreamObserver;
import io.grpc.ManagedChannelBuilder;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class HeadChainReplicaImpl extends HeadChainReplicaGrpc.HeadChainReplicaImplBase {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;
    public ReentrantLock lock;

    HeadChainReplicaImpl(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node, ReentrantLock lock){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
        this.lock = lock;

    }

    @Override
    public void increment(IncRequest request, StreamObserver<HeadResponse> responseObserver) {
        this.lock.lock();
        System.out.println("I got the increment request.");
        String key = request.getKey();
        int value = request.getIncValue();
        if(this.node.amIHead == Boolean.FALSE){
            this.lock.unlock();
            HeadResponse response = HeadResponse.newBuilder().setRc(1).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else{
            this.node.incrementTable(key, value);
            Integer XId =null;
            if(this.node.lastXIdSeen == null){
                XId = 0;
            }
            else{
                XId = this.node.lastXIdSeen + 1;
            }
            node.updateLastXidSeen(XId);
            if(node.amITail == Boolean.TRUE){
                //I am the tail as well.
                this.node.updateLastXidAck(XId);
                this.lock.unlock();
                HeadResponse response = HeadResponse.newBuilder().setRc(0).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
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

                    UpdateRequest updateRequest = UpdateRequest.newBuilder().setKey(key).setNewValue(this.node.nodeState.get(key)).setXid(XId).build();
                    this.node.sentMessages.add(updateRequest);
                    this.node.incReqToClient.put(updateRequest, responseObserver);
                    UpdateResponse updateResponse = stub.update(updateRequest);
                    this.lock.unlock();
                }
                catch (Exception e){
                    this.lock.unlock();
                    System.out.println(e.getMessage());
                }
            }
        }

    }
}
