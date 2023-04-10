package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.GetRequest;
import edu.sjsu.cs249.chain.GetResponse;
import edu.sjsu.cs249.chain.TailChainReplicaGrpc;
import io.grpc.stub.StreamObserver;

public class TailChainReplicaImpl extends TailChainReplicaGrpc.TailChainReplicaImplBase {
    @Override
    public void get(GetRequest request, StreamObserver<GetResponse> responseObserver) {
        super.get(request, responseObserver);
    }
}
