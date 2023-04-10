package edu.sjsu.cs249.zooleader;

import edu.sjsu.cs249.zooleader.Grpc;
import edu.sjsu.cs249.zooleader.ZooLunchGrpc;
import static edu.sjsu.cs249.zooleader.ZooLunchGrpc.*;

import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;
import java.util.concurrent.*;
import java.util.*;


public class Main {
    public static ZooKeeper zk;
    public static Boolean isCurrentLeader = Boolean.FALSE;
    public static List<Long> czxids_of_lunch_attend = new ArrayList<Long>();
    public static Boolean skip_next_lunch = Boolean.FALSE;
    public static Boolean isPrevLeader = Boolean.FALSE;
    public static HashMap<Long, String> zxid_to_leader = new HashMap<Long, String>();
    public static HashMap<Long, List<String>> zxid_to_attendes = new HashMap<Long, List<String>>();
    public static class ZooLunchServiceImpl extends ZooLunchImplBase {
        private String zookeeper_server_list;
        private String lunchPath;
        private String name;

        ZooLunchServiceImpl(String zookeeper_server_list, String lunchPath, String name) {
            this.zookeeper_server_list = zookeeper_server_list;
            this.lunchPath = lunchPath;
            this.name = name;
        }

        @Override
        public void goingToLunch(Grpc.GoingToLunchRequest request, StreamObserver<Grpc.GoingToLunchResponse> responseObserver) {
            try{
                Long lastOrCurrentLunchZxid = new Long(-10000000);
                for(HashMap.Entry<Long, List<String>> lunch: zxid_to_attendes.entrySet()) {
                    if(lunch.getKey() > lastOrCurrentLunchZxid){
                        lastOrCurrentLunchZxid = lunch.getKey();
                    }
                }
                String Leader = zxid_to_leader.get(lastOrCurrentLunchZxid);
                if(Leader != this.name){
                    Grpc.GoingToLunchResponse response = Grpc.GoingToLunchResponse.newBuilder().setRc(1).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                else{
                    List<String> attendes = zxid_to_attendes.get(lastOrCurrentLunchZxid);
                    Grpc.GoingToLunchResponse response = Grpc.GoingToLunchResponse.newBuilder().setRc(0).setRestaurant("Dominos").setLeader("zk-" + this.name).addAllAttendees(attendes).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }

            }
            catch(Exception e){
                System.out.println("Error in processing goingToLunch request");
            }
        }

        @Override
        public void lunchesAttended(Grpc.LunchesAttendedRequest request, StreamObserver<Grpc.LunchesAttendedResponse> responseObserver) {
            Grpc.LunchesAttendedResponse response = Grpc.LunchesAttendedResponse.newBuilder().addAllZxids(czxids_of_lunch_attend).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();

        }

        @Override
        public void getLunch(Grpc.GetLunchRequest request, StreamObserver<Grpc.GetLunchResponse> responseObserver) {
            long lunch_zxid = request.getZxid();
            try{
                if(zxid_to_leader.get(lunch_zxid) == this.name){
                    List<String> attendes = zxid_to_attendes.get(lunch_zxid);
                    Grpc.GetLunchResponse response = Grpc.GetLunchResponse.newBuilder().setRc(0).setLeader("zk-" + this.name).setRestaurant("Dominos").addAllAttendees(attendes).build();
                    responseObserver.onNext(response);
                    responseObserver.onCompleted();
                }
                else{
                    List<String> attendes = zxid_to_attendes.get(lunch_zxid);
                    Boolean didAttend = Boolean.FALSE;
                    for(int i = 0; i<attendes.size(); i++){
                        if(attendes.get(i) == "zk-" + this.name){
                            didAttend = Boolean.TRUE;
                        }
                    }

                    if(didAttend == Boolean.TRUE){
                        String leader = zxid_to_leader.get(lunch_zxid);
                        Grpc.GetLunchResponse response = Grpc.GetLunchResponse.newBuilder().setRc(1).setLeader(leader).build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                    else{
                        Grpc.GetLunchResponse response = Grpc.GetLunchResponse.newBuilder().setRc(2).build();
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                }

            }
            catch(Exception e){

            }
        }

        @Override
        public void skipLunch(Grpc.SkipRequest request, StreamObserver<Grpc.SkipResponse> responseObserver) {
            skip_next_lunch = Boolean.TRUE;
            Grpc.SkipResponse response = Grpc.SkipResponse.newBuilder().build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        }

        @Override
        public void exitZoo(Grpc.ExitRequest request, StreamObserver<Grpc.ExitResponse> responseObserver) {
            System.exit(0);
        }
    }
    public static class ServerCLI extends Thread {
        private String grpcHostPort;
        private String zookeeper_server_list;
        private String Name;
        private String lunchPath;
        ServerCLI(String grpcHostPort, String Name, String serverPorts, String lunchPath) {
            this.grpcHostPort = grpcHostPort;
            this.zookeeper_server_list = serverPorts;
            this.Name = Name;
            this.lunchPath = lunchPath;
        }

        @Override
        public void run() {
            var lastColon = grpcHostPort.lastIndexOf(':');
            var host = grpcHostPort.substring(0, lastColon);
            int serverPort = Integer.parseInt(grpcHostPort.substring(lastColon+1));
            System.out.printf("will contact %s\n", serverPort);
            try{
                Server server = ServerBuilder
                        .forPort(serverPort)
                        .addService(new ZooLunchServiceImpl(this.zookeeper_server_list, this.lunchPath, this.Name)).build();

                server.start();
                server.awaitTermination();

            }
            catch (Exception e){
                System.out.println("Error in connecting the server");
            }

        }
    }
    public static class ZooLunchReader {
        private String zookeeper_server_list;
        private String lunchPath;
        private String name;
        private String grpcHostPort;

        ZooLunchReader(String zookeeper_server_list, String lunchPath, String name, String grpcHostPort) {
            this.zookeeper_server_list = zookeeper_server_list;
            this.lunchPath = lunchPath;
            this.name = name;
            this.grpcHostPort = grpcHostPort;
            try{
                zk = new ZooKeeper(zookeeper_server_list, 10000, (e) ->{System.out.println(e);});
            }
            catch(Exception e){
                System.out.println("Error in creating a zookeeper instance.");
            }
        }

        public Boolean addEntryInTheCompanyDirectory() {
            String path = this.lunchPath + "/employee/" + "zk-" + this.name;
            String data = this.grpcHostPort;
            try{
                zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                return Boolean.TRUE;
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Failed in adding entry to the company directory");
                return Boolean.FALSE;
            }

        }

        private Boolean createZNodeForLunch() {
            try{
                zk.create(this.lunchPath + "/zk-" + this.name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                return Boolean.TRUE;
            }
            catch (Exception e) {
                System.out.println(e.getMessage());
                System.out.println("Error in creating znode for getting ready for lunch.");
                return Boolean.FALSE;
            }
        }

        public class electLeader implements Runnable {
            private ZooKeeper zk;
            private String lunchPath;
            private String name;
            public electLeader(ZooKeeper zk, String lunchPath, String name){
                this.zk = zk;
                this.lunchPath = lunchPath;
                this.name = name;
            }
            @Override
            public void run() {
                long waiting_time = 0;
                try{
                    waiting_time = this.zk.getChildren(this.lunchPath, true).size();
                }
                catch (Exception e){
                    System.out.println("Error in getting the number of znode under /lunch directory.");
                }
                while(true){
                    if(doesPathExist(this.lunchPath + "/readyforlunch") == Boolean.FALSE){
                        break;
                    }
                    try{
                        if(isPrevLeader == Boolean.TRUE){
                            while(waiting_time > 0){
                                Thread.sleep(waiting_time*1000);
                                waiting_time = waiting_time - 1;
                            }
                            isPrevLeader = Boolean.FALSE;
                        }
                        this.zk.create(this.lunchPath + "/leader", this.name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                        System.out.println("I have become the leader");
                        isCurrentLeader = Boolean.TRUE;
                        break;
                    }
                    catch(Exception e){
                        System.out.println("Not able to become leader this time. I will become next time");
                        if(waiting_time > 0){
                            try {
                                Thread.sleep(waiting_time*1000);
                                System.out.println("I will wait for following seconds to get elected:" + waiting_time);
                                waiting_time = waiting_time - 1;
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                        else{
                            break;
                        }

                    }

                }
            }
        };

        private void chooseLeader() {
            electLeader el = new electLeader(zk, this.lunchPath, this.name);
            FutureTask<String> choosingLeader = new FutureTask<>(el, "");
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(choosingLeader);
        }

        private void deleteZNodeForLunch() {
            System.out.println("delete znode for lunch called.");
            String deletePath = this.lunchPath + "/zk-" + this.name;
            try{
                if(doesPathExist(deletePath)){
                    int version = zk.exists(deletePath, true).getVersion();
                    zk.delete(deletePath, version);
                }
                else{
                    System.out.println("Path does not exist for the node to be deleted.");
                }
            }
            catch (Exception e){
                System.out.println("Not able to delete the ZNode after lunch is finished");
            }
        }

        private Boolean doesPathExist(String path){
            try{
                if(zk.exists(path, true) != null){
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            catch(Exception e){
                return Boolean.FALSE;
            }
        }

        public void addAttendedAndIncrementLunchesAttended() {
            try{
                System.out.println("incrementing lunches attended.");
                Stat attendesLowerBound = zk.exists(this.lunchPath + "/readyforlunch", true);
                Stat attendesUpperBound = zk.exists(this.lunchPath + "/lunchtime", true);
                Stat lunchZNode = zk.exists(this.lunchPath + "/zk-" + this.name, true);

                List<String> lunchChildren = zk.getChildren(this.lunchPath, true);
                List<String> attendes = new ArrayList<String>();

                for(int i = 0; i<lunchChildren.size(); i++){
                    String child = lunchChildren.get(i);
                    Stat childStat = zk.exists(this.lunchPath + "/" + child, true);
                    if(childStat != null && childStat.getCzxid() > attendesLowerBound.getCzxid() &&
                            childStat.getCzxid() < attendesUpperBound.getCzxid()) {
                        attendes.add(child);
                    }
                }

                zxid_to_attendes.put(attendesUpperBound.getCzxid(), attendes);

                if(lunchZNode != null && lunchZNode.getCzxid() > attendesLowerBound.getCzxid() &&
                lunchZNode.getCzxid() < attendesUpperBound.getCzxid()) {
                    czxids_of_lunch_attend.add(attendesUpperBound.getCzxid());
                }
            }
            catch (Exception e){
                System.out.println(e.getMessage());
                System.out.println("Error in incrementing number of lunches attended");
            }
        }

        public void findLunchLeader() {
            try{
                Stat lunchTime = zk.exists(this.lunchPath + "/lunchtime", true);
                Stat lunchLeader = zk.exists(this.lunchPath + "/leader", true);
                byte[] leaderName = zk.getData(this.lunchPath + "/leader", true, lunchLeader);
                zxid_to_leader.put(lunchTime.getCzxid(), leaderName.toString());
            }
            catch(Exception e){
                System.out.println("Error in finding lunch leader");
            }
        }

        public void deleteLeaderNode(){
            try{
                String deletePath = this.lunchPath + "/leader";
                if(doesPathExist(deletePath)){
                    int version = zk.exists(deletePath, true).getVersion();
                    zk.delete(deletePath, version);
                }
                else{
                    System.out.println("Path does not exist for the node to be deleted.");
                }

            }
            catch (Exception e){
                System.out.println("Error in deleting leader node.");
            }
        }

        public void readyForLunch() {
            String createWatchPath = this.lunchPath + "/readyforlunch";
            String lunchTimeWatchPath = this.lunchPath + "/lunchtime";
            String DeleteWatchPath = this.lunchPath + "/lunchtime";
            String DeleteWatchPath2 = this.lunchPath + "/readyforlunch";
            Watcher watchForCreate = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    try{
                        zk.exists(createWatchPath, this);
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(skip_next_lunch == Boolean.TRUE){
                        skip_next_lunch = Boolean.FALSE;
                        return;
                    }
                    if (watchedEvent.getPath().equals(createWatchPath) && watchedEvent.getType().equals(Event.EventType.NodeCreated)){
                        createZNodeForLunch();
                        chooseLeader();
                    }
                }
            };
            try{
                zk.exists(createWatchPath, watchForCreate);
            }
            catch(Exception e){
                System.out.println("Error adding watch for creating znode.");
            }

            Watcher watchForLunchTime = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    try{
                        zk.exists(lunchTimeWatchPath, this);
                    }
                    catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    if(watchedEvent.getPath().equals(lunchTimeWatchPath) && watchedEvent.getType() == Event.EventType.NodeCreated){
                        addAttendedAndIncrementLunchesAttended();
                        findLunchLeader();
                    }

                }
            };
            try{
                zk.exists(lunchTimeWatchPath, watchForLunchTime);
            }
            catch (Exception e){
                System.out.println("Error adding watch for lunch time.");
            }

            Watcher watchForDelete = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if((watchedEvent.getPath().equals(DeleteWatchPath))  && watchedEvent.getType() == Event.EventType.NodeDeleted){
                        deleteZNodeForLunch();
                        if(isCurrentLeader == Boolean.TRUE){
                            deleteLeaderNode();
                            isCurrentLeader = Boolean.FALSE;
                            isPrevLeader = Boolean.TRUE;
                        }
                    }
                }
            };
            try {
                zk.addWatch(DeleteWatchPath, watchForDelete, AddWatchMode.PERSISTENT);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Error adding watch for deleting znode.");
            }

            Watcher watchForDelete2 = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if((watchedEvent.getPath().equals(DeleteWatchPath2))  && watchedEvent.getType() == Event.EventType.NodeDeleted){
                        deleteZNodeForLunch();
                        if(isCurrentLeader == Boolean.TRUE){
                            deleteLeaderNode();
                            isCurrentLeader = Boolean.FALSE;
                            isPrevLeader = Boolean.TRUE;
                        }
                    }
                }
            };
            try {
                zk.addWatch(DeleteWatchPath2, watchForDelete2, AddWatchMode.PERSISTENT);
            }
            catch(Exception e){
                System.out.println(e.getMessage());
                System.out.println("Error adding watch for deleting znode.");
            }



        }
    }
    @Command(description = "First version of ZooLunchReader")
    public static class Cli implements Callable<Integer> {

        @Parameters(index = "0", description = "name")
        private String Name;

        @Parameters(index = "1", description = "host port of grpc")
        private String grpcHostPort;

        @Parameters(index = "2", description = "list of zookeeper servers")
        private String serverPorts;

        @Parameters(index = "3", description = "path of lunch")
        private String lunchPath;

        @Override
        public Integer call() throws Exception {
            ServerCLI server = new ServerCLI(grpcHostPort, Name, serverPorts, lunchPath);
            server.start();

            ZooLunchReader zoolunch = new ZooLunchReader(serverPorts, lunchPath, Name, grpcHostPort);
            zoolunch.addEntryInTheCompanyDirectory();
            zoolunch.readyForLunch();

            server.join();

            return 0;
        }

    };
    public static void main(String[] args)
    {
        System.exit(new CommandLine(new Cli()).execute(args));
    }
}