package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.*;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static org.apache.zookeeper.CreateMode.*;

public class ChainNode {
    private String name;
    private String grpcHostPort;
    private String zkHostPorts;
    private String controlPath;
    private Long chainNodeSequenceNumber;
    public  ZooKeeper zk;
    public String predecessorNode = null;
    public String successorNode = null;
    public HashMap<String, Integer> nodeState;
    public HashMap<UpdateRequest, StreamObserver<HeadResponse>> incReqToClient;
    public Boolean amIHead = Boolean.FALSE;
    public Boolean amITail = Boolean.FALSE;
    public List<UpdateRequest> sentMessages = new ArrayList<UpdateRequest>();
    Integer lastXIdSeen = null;
    Integer lastXidAck = null;
    Long lastZxIdSeen = null;
    public ReentrantLock lock;

    ChainNode(String name, String grpcHostPort, String zkHostPorts, String controlPath, ReentrantLock lock){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        this.nodeState = new HashMap<String, Integer>();
        this.incReqToClient = new HashMap<UpdateRequest, StreamObserver<HeadResponse>>();
        this.lock = lock;

        try {
            this.zk = new ZooKeeper(zkHostPorts, 10000, (e) -> {System.out.println(e);});
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    Watcher controlPathWatcher = new Watcher() {
        @Override
        public void process(WatchedEvent watchedEvent) {
            try{
                List<String> children =  zk.getChildren(controlPath, this);
                Stat baseDirectory = zk.exists(controlPath, true);
                lastZxIdSeen = baseDirectory.getPzxid();
            }
            catch (Exception e){

            }
            if(watchedEvent.getPath().equals(controlPath) && watchedEvent.getType() == Event.EventType.NodeChildrenChanged){
                lock.lock();
                findPredecessor();
                findSuccessor();
                if(predecessorNode != null){
                    try{
                        Stat PredecessorNode = zk.exists(controlPath + "/" + predecessorNode, true);
                        byte[] nodeDataBytes = zk.getData(controlPath + "/" + predecessorNode, true, PredecessorNode);
                        String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                        int newLineIndex = nodeData.indexOf("\n");
                        String predHostPort = nodeData.substring(0, newLineIndex);

                        var lastColon = predHostPort.lastIndexOf(':');
                        var host = predHostPort.substring(0, lastColon);
                        var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                        var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                        var stub = ReplicaGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                        String ZNodeName = Long.toString(chainNodeSequenceNumber);
                        if(ZNodeName.length() < 10){
                            ZNodeName = "0".repeat(10 - ZNodeName.length()).concat(ZNodeName);
                        }
                        ZNodeName = "replica-" + ZNodeName;
                        NewSuccessorRequest request = NewSuccessorRequest.newBuilder().setLastZxidSeen(lastZxIdSeen).setLastXid(lastXIdSeen != null ? lastXIdSeen : -1)
                                .setLastAck(lastXidAck != null ? lastXidAck : -1).setZnodeName(ZNodeName).build();
                        StreamObserver<NewSuccessorResponse> newResponseObserver = new StreamObserver<NewSuccessorResponse>() {
                            @Override
                            public void onNext(NewSuccessorResponse response) {
                                updateNodeState(response);
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                channel.shutdown();
                                try {
                                    channel.awaitTermination(1, TimeUnit.SECONDS);
                                } catch (InterruptedException e) {
                                    System.out.println("Channel didn't shut down properly.");
                                }
                            }

                            @Override
                            public void onCompleted() {
                                channel.shutdown();
                                try {
                                    channel.awaitTermination(1, TimeUnit.SECONDS);
                                } catch (InterruptedException e) {
                                    System.out.println("Channel didn't shut down properly.");
                                }
                            }
                        };
                        stub.newSuccessor(request, newResponseObserver);
                        lock.unlock();

                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        lock.unlock();
                    }
                }
                if(successorNode == null){
                    try{
                        Stat predecessorNodeStat = zk.exists(controlPath + "/" + predecessorNode, true);
                        byte[] nodeDataBytes = zk.getData(controlPath + "/" + predecessorNode, true, predecessorNodeStat);
                        String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                        int newLineIndex = nodeData.indexOf("\n");
                        String predHostPort = nodeData.substring(0, newLineIndex);

                        var lastColon = predHostPort.lastIndexOf(':');
                        var host = predHostPort.substring(0, lastColon);
                        var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                        var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                        var stub = ReplicaGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);

                        for(int i = 0; i<sentMessages.size(); i++){
                            AckRequest request = AckRequest.newBuilder().setXid(sentMessages.get(i).getXid()).build();
                            StreamObserver<AckResponse> newResponseObserver = new StreamObserver<AckResponse>() {
                                @Override
                                public void onNext(AckResponse ackResponse) {

                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    channel.shutdown();
                                    try {
                                        channel.awaitTermination(1, TimeUnit.SECONDS);
                                    } catch (InterruptedException e) {
                                        System.out.println("Channel didn't shut down properly.");
                                    }
                                }

                                @Override
                                public void onCompleted() {
                                    channel.shutdown();
                                    try {
                                        channel.awaitTermination(1, TimeUnit.SECONDS);
                                    } catch (InterruptedException e) {
                                        System.out.println("Channel didn't shut down properly.");
                                    }
                                }
                            };
                            stub.ack(request, newResponseObserver);
                            lock.unlock();
                        }


                    }
                    catch (Exception e){
                        System.out.println(e.getMessage());
                        lock.unlock();
                    }
                }
                lock.unlock();

            }
        }

    };

    public ZooKeeper getZooKeeperInstance() {
        return this.zk;
    }

    public Boolean createChainNode() {
        try{
            String data = this.grpcHostPort + "\n" + this.name;
            String chainNodePath = this.zk.create(this.controlPath + "/replica-", data.getBytes(StandardCharsets.UTF_8), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL_SEQUENTIAL);
            this.chainNodeSequenceNumber = Long.parseLong(chainNodePath.substring(chainNodePath.length() - 10));
            List<String> chainNodes = this.zk.getChildren(this.controlPath, controlPathWatcher);
            return Boolean.TRUE;

        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
    }

    private long getNodeSequenceNumber(String node) {
        return Long.parseLong(node.substring(8));
    }

    public void updateNodeState(NewSuccessorResponse response){
        if(response.getRc() == 0){
            this.nodeState = new HashMap<>(response.getStateMap());
            this.sentMessages = new ArrayList<>(response.getSentList());
            this.lastXIdSeen = response.getLastXid();
        }
        else if(response.getRc() == 1){
            this.sentMessages.addAll(response.getSentList());
            this.lastXIdSeen = response.getLastXid();
        }
        else{
            System.out.println("The predecessor refused to accept me as a successor.");
        }
    }

    public void findPredecessor() {
        try{
            Stat baseDirectory = this.zk.exists(this.controlPath, true);
            this.lastZxIdSeen = baseDirectory.getPzxid();
            this.predecessorNode = null;
            List<String> chainNodes = this.zk.getChildren(this.controlPath, true);

            for(int i = 0; i < chainNodes.size(); i++){
                if(getNodeSequenceNumber(chainNodes.get(i)) < this.chainNodeSequenceNumber &&
                        (this.predecessorNode == null || getNodeSequenceNumber(this.predecessorNode) < getNodeSequenceNumber(chainNodes.get(i)))){
                    this.predecessorNode = chainNodes.get(i);
                }
            }

            if(this.predecessorNode != null){
                this.amIHead = Boolean.FALSE;
                try{
                    Stat PredecessorNode = zk.exists(this.controlPath + "/" + this.predecessorNode, true);
                    byte[] nodeDataBytes = zk.getData(this.controlPath + "/" + this.predecessorNode, true, PredecessorNode);
                    String nodeData = new String(nodeDataBytes, StandardCharsets.UTF_8);
                    int newLineIndex = nodeData.indexOf("\n");
                    String predHostPort = nodeData.substring(0, newLineIndex);

                    var lastColon = predHostPort.lastIndexOf(':');
                    var host = predHostPort.substring(0, lastColon);
                    var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                    var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                    var stub = ReplicaGrpc.newStub(channel).withDeadlineAfter(3, TimeUnit.SECONDS);
                    String ZNodeName = Long.toString(this.chainNodeSequenceNumber);
                    if(ZNodeName.length() < 10){
                        ZNodeName = "0".repeat(10 - ZNodeName.length()).concat(ZNodeName);
                    }
                    ZNodeName = "replica-" + ZNodeName;
                    NewSuccessorRequest request = NewSuccessorRequest.newBuilder().setLastZxidSeen(this.lastZxIdSeen).setLastXid(this.lastXIdSeen != null ? this.lastXIdSeen : -1)
                            .setLastAck(this.lastXidAck != null ? this.lastXidAck : -1).setZnodeName(ZNodeName).build();
                    StreamObserver<NewSuccessorResponse> newResponseObserver = new StreamObserver<NewSuccessorResponse>() {
                        @Override
                        public void onNext(NewSuccessorResponse response) {
                            updateNodeState(response);
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            channel.shutdown();
                            try {
                                channel.awaitTermination(1, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                System.out.println("Channel didn't shut down properly.");
                            }
                        }

                        @Override
                        public void onCompleted() {
                            channel.shutdown();
                            try {
                                channel.awaitTermination(1, TimeUnit.SECONDS);
                            } catch (InterruptedException e) {
                                System.out.println("Channel didn't shut down properly.");
                            }
                        }
                    };
                    stub.newSuccessor(request, newResponseObserver);

                }
                catch (Exception e){
                    System.out.println(e.getMessage());
                }
            }
            else{
                this.amIHead = Boolean.TRUE;
                System.out.println("I am the head");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void findSuccessor() {
        try{
            Stat baseDirectory = this.zk.exists(this.controlPath, true);
            this.lastZxIdSeen = baseDirectory.getPzxid();
            this.successorNode = null;
            List<String> chainNodes = this.zk.getChildren(this.controlPath, true);

            for(int i = 0; i < chainNodes.size(); i++){
                if(getNodeSequenceNumber(chainNodes.get(i)) > this.chainNodeSequenceNumber &&
                        (this.successorNode == null || getNodeSequenceNumber(this.successorNode) > getNodeSequenceNumber(chainNodes.get(i)))){
                    this.successorNode = chainNodes.get(i);
                }
            }

            if(this.successorNode != null){
                this.amITail = Boolean.FALSE;
            }
            else{
                this.amITail = Boolean.TRUE;
                System.out.println("I am the tail.");
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void incrementTable(String key, int value) {
        int prevValue = this.nodeState.getOrDefault(key, 0);
        this.nodeState.put(key, value + prevValue);
    }

    public void updateTable(String key, int value) {
        this.nodeState.put(key, value);
    }

    public void updateLastXidSeen(Integer XId) {
        this.lastXIdSeen = XId;
    }

    public void updateLastXidAck(Integer XId){
        this.lastXidAck = XId;
    }
}