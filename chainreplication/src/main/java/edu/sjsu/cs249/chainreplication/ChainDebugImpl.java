package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.stub.StreamObserver;


public class ChainDebugImpl extends ChainDebugGrpc.ChainDebugImplBase {
    public ChainNode node;

    ChainDebugImpl(ChainNode node){
        this.node = node;
    }

    @Override
    public void debug(ChainDebugRequest request, StreamObserver<ChainDebugResponse> responseObserver) {
        ChainDebugResponse response = ChainDebugResponse.newBuilder().setXid(this.node.lastXIdSeen).putAllState(this.node.nodeState)
                .addAllSent(this.node.sentMessages).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exit(ExitRequest request, StreamObserver<ExitResponse> responseObserver) {
        System.exit(0);
    }
}
