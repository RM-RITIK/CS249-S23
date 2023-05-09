package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.locks.ReentrantLock;


public class ChainDebugImpl extends ChainDebugGrpc.ChainDebugImplBase {
    public ChainNode node;
    ReentrantLock lock;

    ChainDebugImpl(ChainNode node, ReentrantLock lock){
        this.node = node;
        this.lock = lock;
    }

    @Override
    public void debug(ChainDebugRequest request, StreamObserver<ChainDebugResponse> responseObserver) {
        this.lock.lock();
        ChainDebugResponse response = ChainDebugResponse.newBuilder().setXid(this.node.lastXIdSeen).putAllState(this.node.nodeState)
                    .addAllSent(this.node.sentMessages).build();
        this.lock.unlock();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exit(ExitRequest request, StreamObserver<ExitResponse> responseObserver) {
        System.exit(0);
    }
}
