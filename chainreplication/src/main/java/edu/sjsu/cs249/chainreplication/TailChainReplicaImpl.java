package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.GetRequest;
import edu.sjsu.cs249.chain.GetResponse;
import edu.sjsu.cs249.chain.TailChainReplicaGrpc;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.locks.ReentrantLock;


public class TailChainReplicaImpl extends TailChainReplicaGrpc.TailChainReplicaImplBase {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;
    public ReentrantLock lock;

    TailChainReplicaImpl(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node, ReentrantLock lock){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
        this.lock = lock;
    }
    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        this.lock.lock();
        String key = request.getKey();
        if(this.node.amITail == Boolean.FALSE){
            this.lock.unlock();
            GetResponse response = GetResponse.newBuilder().setRc(1).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else{
            if(this.node.nodeState.containsKey(key)){
                this.lock.unlock();
                GetResponse response= GetResponse.newBuilder().setRc(0).setValue(this.node.nodeState.get(key)).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
                this.lock.unlock();
                GetResponse response = GetResponse.newBuilder().setRc(0).setValue(0).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }
    }
}
