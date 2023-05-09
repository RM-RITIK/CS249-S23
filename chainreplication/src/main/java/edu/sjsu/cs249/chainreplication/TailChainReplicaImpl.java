package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.GetRequest;
import edu.sjsu.cs249.chain.GetResponse;
import edu.sjsu.cs249.chain.TailChainReplicaGrpc;
import io.grpc.stub.StreamObserver;

public class TailChainReplicaImpl extends TailChainReplicaGrpc.TailChainReplicaImplBase {
    public String name;
    public String grpcHostPort;
    public String zkHostPorts;
    public String controlPath;
    public ChainNode node;

    TailChainReplicaImpl(String name, String grpcHostPort, String zkHostPorts, String controlPath, ChainNode node){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.node = node;
    }
    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        String key = request.getKey();
        if(this.node.amITail == Boolean.FALSE){
            GetResponse response = GetResponse.newBuilder().setRc(1).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }
        else{
            if(this.node.nodeState.containsKey(key)){
                GetResponse response= GetResponse.newBuilder().setRc(0).setValue(this.node.nodeState.get(key)).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
                GetResponse response = GetResponse.newBuilder().setRc(0).setValue(0).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
        }
    }
}
