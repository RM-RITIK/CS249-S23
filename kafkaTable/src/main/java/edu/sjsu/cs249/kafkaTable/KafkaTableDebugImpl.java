package edu.sjsu.cs249.kafkaTable;

import io.grpc.stub.StreamObserver;

import java.util.concurrent.locks.ReentrantLock;

public class KafkaTableDebugImpl extends KafkaTableDebugGrpc.KafkaTableDebugImplBase {
    public Replica replica;
    public ReentrantLock lock;
    KafkaTableDebugImpl(Replica replica, ReentrantLock lock){
        this.replica = replica;
        this.lock = lock;
    }

    @Override
    public void debug(KafkaTableDebugRequest request, StreamObserver<KafkaTableDebugResponse> responseObserver) {
        this.lock.lock();
        Snapshot stateSnapshot = Snapshot.newBuilder().setReplicaId(this.replica.replicaName)
                .putAllTable(this.replica.state)
                .setOperationsOffset(this.replica.operationsOffset)
                .putAllClientCounters(this.replica.clientCounter)
                .setSnapshotOrderingOffset(this.replica.snapshotOffset).build();
        KafkaTableDebugResponse response = KafkaTableDebugResponse.newBuilder().setSnapshot(stateSnapshot).build();
        this.lock.unlock();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void exit(ExitRequest request, StreamObserver<ExitResponse> responseObserver) {
        System.exit(0);
    }
}
