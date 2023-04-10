package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.stub.StreamObserver;

public class ReplicaImpl extends ReplicaGrpc.ReplicaImplBase {
    @Override
    public void update(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        super.update(request, responseObserver);
    }

    @Override
    public void newSuccessor(NewSuccessorRequest request, StreamObserver<NewSuccessorResponse> responseObserver) {
        super.newSuccessor(request, responseObserver);
    }

    @Override
    public void ack(AckRequest request, StreamObserver<AckResponse> responseObserver) {
        super.ack(request, responseObserver);
    }
}
