package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.HeadChainReplicaGrpc;
import edu.sjsu.cs249.chain.HeadResponse;
import edu.sjsu.cs249.chain.IncRequest;
import io.grpc.stub.StreamObserver;

public class HeadChainReplicaImpl extends HeadChainReplicaGrpc.HeadChainReplicaImplBase {
    @Override
    public void increment(IncRequest request, StreamObserver<HeadResponse> responseObserver) {
        super.increment(request, responseObserver);
    }
}
