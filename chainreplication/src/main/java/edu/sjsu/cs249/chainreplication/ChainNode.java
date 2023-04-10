package edu.sjsu.cs249.chainreplication;

import edu.sjsu.cs249.chain.ReplicaGrpc;
import io.grpc.ManagedChannelBuilder;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.List;

import static org.apache.zookeeper.CreateMode.*;

public class ChainNode {
    private String name;
    private String grpcHostPort;
    private String zkHostPorts;
    private String controlPath;
    private Long chainNodeSequenceNumber;
    public ZooKeeper zk;
    public String predecessorNode = null;
    public String successorNode = null;

    ChainNode(String name, String grpcHostPort, String zkHostPorts, String controlPath){
        this.name = name;
        this.grpcHostPort = grpcHostPort;
        this.zkHostPorts = zkHostPorts;
        this.controlPath = controlPath;
        try {
            this.zk = new ZooKeeper(zkHostPorts, 10000, (e) -> {System.out.println(e);});
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ZooKeeper getZooKeeperInstance() {
        return this.zk;
    }

    public Boolean createChainNode() {
        try{
            String data = this.grpcHostPort + "\n" + this.name;
            String chainNodePath = this.zk.create(this.controlPath + "/replica-", data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, EPHEMERAL_SEQUENTIAL);
            this.chainNodeSequenceNumber = Long.parseLong(chainNodePath.substring(chainNodePath.length() - 10));
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

    public Boolean addWatchForPredecessorNode(String node) {
        String predecessorPath = this.controlPath + "/" + node;
        Watcher watchForPredecessor = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getPath().equals(predecessorPath) && watchedEvent.getType() == Event.EventType.NodeDeleted){
                    findPredecessor();
                    if(predecessorNode != null){
                        try{
                            Stat newPredecessorNode = zk.exists(controlPath + "/" + predecessorNode, true);
                            byte[] nodeDataBytes = zk.getData(controlPath + "/" + predecessorPath, true, newPredecessorNode);
                            String nodeData = nodeDataBytes.toString();
                            int newLineIndex = nodeData.indexOf("\n");
                            String predHostPort = nodeData.substring(0, newLineIndex);

                            var lastColon = predHostPort.lastIndexOf(':');
                            var host = predHostPort.substring(0, lastColon);
                            var port = Integer.parseInt(predHostPort.substring(lastColon+1));
                            var channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
                            var stub = ReplicaGrpc.newBlockingStub(channel);

                            //send a message to the predecessor that I am the new successor via
                            //blocking stub.

                        }
                        catch (Exception e){
                            System.out.println(e.getMessage());
                        }
                    }

                }
            }
        };

        try{
            this.zk.addWatch(predecessorPath, watchForPredecessor, AddWatchMode.PERSISTENT);
            return Boolean.TRUE;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }
    }

    public Boolean addWatchForSuccessorNode(String node){
        String successorPath = this.controlPath + "/" + node;

        Watcher watchForSuccessor = new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                if(watchedEvent.getPath().equals(successorPath) && watchedEvent.getType() == Event.EventType.NodeDeleted){
                    findSuccessor();
                    if(successorNode != null){
                        //do some action
                    }
                }
            }
        };

        try{
            this.zk.addWatch(successorPath, watchForSuccessor, AddWatchMode.PERSISTENT);
            return Boolean.TRUE;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return Boolean.FALSE;
        }

    }

    public void findPredecessor() {
        try{
            List<String> chainNodes = this.zk.getChildren(this.controlPath, true);

            for(int i = 0; i < chainNodes.size(); i++){
                if(getNodeSequenceNumber(chainNodes.get(i)) < this.chainNodeSequenceNumber &&
                        (this.predecessorNode == null || getNodeSequenceNumber(this.predecessorNode) < getNodeSequenceNumber(chainNodes.get(i)))){
                    this.predecessorNode = chainNodes.get(i);
                }
            }

            if(this.predecessorNode != null){
                this.addWatchForPredecessorNode(this.predecessorNode);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void findSuccessor() {
        try{
            List<String> chainNodes = this.zk.getChildren(this.controlPath, true);

            for(int i = 0; i < chainNodes.size(); i++){
                if(getNodeSequenceNumber(chainNodes.get(i)) > this.chainNodeSequenceNumber &&
                        (this.successorNode == null || getNodeSequenceNumber(this.successorNode) > getNodeSequenceNumber(chainNodes.get(i)))){
                    this.successorNode = chainNodes.get(i);
                }
            }

            if(this.successorNode != null){
                this.addWatchForSuccessorNode(this.successorNode);
            }

        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}
