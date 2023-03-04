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
    public static boolean isLeader = Boolean.FALSE;
    public static ZooKeeper zk;
    public static class ZooLunchServiceImpl extends ZooLunchImplBase {
        private ZooKeeper zk;
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
            if(isLeader == Boolean.FALSE) {
                Grpc.GoingToLunchResponse response = Grpc.GoingToLunchResponse.newBuilder().setRc(1).build();
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            }
            else{
                try{
                }
                catch(Exception e){
                    System.out.println("Error in creating a zookeeper instance.");
                }

            }
        }

        @Override
        public void lunchesAttended(Grpc.LunchesAttendedRequest request, StreamObserver<Grpc.LunchesAttendedResponse> responseObserver) {
            super.lunchesAttended(request, responseObserver);
        }

        @Override
        public void getLunch(Grpc.GetLunchRequest request, StreamObserver<Grpc.GetLunchResponse> responseObserver) {
            super.getLunch(request, responseObserver);
        }

        @Override
        public void skipLunch(Grpc.SkipRequest request, StreamObserver<Grpc.SkipResponse> responseObserver) {
            super.skipLunch(request, responseObserver);
        }

        @Override
        public void exitZoo(Grpc.ExitRequest request, StreamObserver<Grpc.ExitResponse> responseObserver) {
            super.exitZoo(request, responseObserver);
        }
    }
    public static class ServerCLI extends Thread {
        private int serverPort;
        private String zookeeper_server_list;
        private String Name;
        private String lunchPath;
        ServerCLI(int serverPort, String Name, String serverPorts, String lunchPath) {
            this.serverPort = serverPort;
            this.zookeeper_server_list = serverPorts;
            this.Name = Name;
            this.lunchPath = lunchPath;
        }

        @Override
        public void run() {
            System.out.printf("will contact %s\n", this.serverPort);
            try{
                Server server = ServerBuilder
                        .forPort(this.serverPort)
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
        private int grpcHostPort;

        private ZooKeeper zk;

        ZooLunchReader(String zookeeper_server_list, String lunchPath, String name) {
            this.zookeeper_server_list = zookeeper_server_list;
            this.lunchPath = lunchPath;
            this.name = name;
            try{
                this.zk = new ZooKeeper(zookeeper_server_list, 10000, (e) ->{System.out.println(e);});
            }
            catch(Exception e){
                System.out.println("Error in creating a zookeeper instance.");
            }
        }

        public Boolean addEntryInTheCompanyDirectory() {
            String path = this.lunchPath + "/employee/" + "zk-" + this.name;
            String data = "172.27.24.5:" + String.valueOf(this.grpcHostPort);
            try{
                this.zk.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
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
                this.zk.create(this.lunchPath + "/zk-" + this.name, name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                return Boolean.TRUE;
            }
            catch (Exception e) {
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
                    try{
                        this.zk.create(this.lunchPath + "/leader", this.name.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
                        System.out.println("I have become the leader");
                        isLeader = Boolean.TRUE;
                        break;
                    }
                    catch(Exception e){
                        System.out.println("Not able to become leader this time. I will become next time");
                        if(waiting_time > 0){
                            try {
                                Thread.sleep(waiting_time*1000);
                                waiting_time = waiting_time - 1;
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    }

                }
            }
        };

        private void chooseLeader() {
            electLeader el = new electLeader(this.zk, this.lunchPath, this.name);
            FutureTask<String> choosingLeader = new FutureTask<>(el, "");
            ExecutorService executor = Executors.newFixedThreadPool(1);
            executor.submit(choosingLeader);
        }

        private void deleteZNodeForLunch() {
            String deletePath = this.lunchPath + "/zk-" + this.name;
            try{
                int version = this.zk.exists(deletePath, true).getVersion();
                this.zk.delete(deletePath, version);
            }
            catch (Exception e){
                System.out.println("Not able to delete the ZNode after lunch is finished");
            }
        }

        private Boolean doesPathExist(String path){
            try{
                if(this.zk.exists(path, true) != null){
                    return Boolean.TRUE;
                }
                return Boolean.FALSE;
            }
            catch(Exception e){
                return Boolean.FALSE;
            }
        }

        public void readyForLunch() {
            String createWatchPath = "/lunch/readyforlunch";
            String DeleteWatchPath = "/lunch/lunchtime";
            Watcher watchForCreate = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getPath().equals(createWatchPath) && watchedEvent.getType() == Event.EventType.NodeCreated){
                        createZNodeForLunch();
                        chooseLeader();
                    }
                }
            };
            try{
                this.zk.addWatch(createWatchPath, watchForCreate, AddWatchMode.PERSISTENT);
            }
            catch(Exception e){
                System.out.println("Error adding watch for creating znode.");
            }
            Watcher watchForDelete = new Watcher() {
                @Override
                public void process(WatchedEvent watchedEvent) {
                    if(watchedEvent.getPath().equals(DeleteWatchPath) && watchedEvent.getType() == Event.EventType.NodeDeleted){
                        deleteZNodeForLunch();
                    }
                }
            };
            try {
                this.zk.addWatch(DeleteWatchPath, watchForDelete, AddWatchMode.PERSISTENT);
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
        private int grpcHostPort;

        @Parameters(index = "2", description = "list of zookeeper servers")
        private String serverPorts;

        @Parameters(index = "3", description = "path of lunch")
        private String lunchPath;

        @Override
        public Integer call() throws Exception {
            ServerCLI server = new ServerCLI(grpcHostPort, Name, serverPorts, lunchPath);
            server.start();

            ZooLunchReader zoolunch = new ZooLunchReader(serverPorts, lunchPath, Name);
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