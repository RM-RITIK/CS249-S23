package edu.sjsu.cs249.kafkaTable;

import io.grpc.stub.StreamObserver;

public class KafkaTableDebugImpl extends KafkaTableDebugGrpc.KafkaTableDebugImplBase {
    public Replica replica;
    KafkaTableDebugImpl(Replica replica){
        this.replica = replica;
    }

    @Override
    public void debug(KafkaTableDebugRequest request, StreamObserver<KafkaTableDebugResponse> responseObserver) {
        Snapshot stateSnapshot = Snapshot.newBuilder().setReplicaId(this.replica.replicaName)
                .putAllTable(this.replica.state)
                .setOperationsOffset(this.replica.operationsOffset)
                .putAllClientCounters(this.replica.clientCounter)
                .setSnapshotOrderingOffset(this.replica.snapshotOffset).build();
        KafkaTableDebugResponse response = KafkaTableDebugResponse.newBuilder().setSnapshot(stateSnapshot).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exit(ExitRequest request, StreamObserver<ExitResponse> responseObserver) {
        System.exit(0);
    }
}
