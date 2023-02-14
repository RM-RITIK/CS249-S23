package edu.sjsu.cs249.iamhere;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.52.1)",
    comments = "Source: grpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class HereServiceGrpc {

  private HereServiceGrpc() {}

  public static final String SERVICE_NAME = "edu.sjsu.cs249.iamhere.HereService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HereRequest,
      edu.sjsu.cs249.iamhere.Grpc.HereResponse> getHereMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "here",
      requestType = edu.sjsu.cs249.iamhere.Grpc.HereRequest.class,
      responseType = edu.sjsu.cs249.iamhere.Grpc.HereResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HereRequest,
      edu.sjsu.cs249.iamhere.Grpc.HereResponse> getHereMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HereRequest, edu.sjsu.cs249.iamhere.Grpc.HereResponse> getHereMethod;
    if ((getHereMethod = HereServiceGrpc.getHereMethod) == null) {
      synchronized (HereServiceGrpc.class) {
        if ((getHereMethod = HereServiceGrpc.getHereMethod) == null) {
          HereServiceGrpc.getHereMethod = getHereMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.iamhere.Grpc.HereRequest, edu.sjsu.cs249.iamhere.Grpc.HereResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "here"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.iamhere.Grpc.HereRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.iamhere.Grpc.HereResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HereServiceMethodDescriptorSupplier("here"))
              .build();
        }
      }
    }
    return getHereMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HelloRequest,
      edu.sjsu.cs249.iamhere.Grpc.HelloResponse> getHelloMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "hello",
      requestType = edu.sjsu.cs249.iamhere.Grpc.HelloRequest.class,
      responseType = edu.sjsu.cs249.iamhere.Grpc.HelloResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HelloRequest,
      edu.sjsu.cs249.iamhere.Grpc.HelloResponse> getHelloMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.iamhere.Grpc.HelloRequest, edu.sjsu.cs249.iamhere.Grpc.HelloResponse> getHelloMethod;
    if ((getHelloMethod = HereServiceGrpc.getHelloMethod) == null) {
      synchronized (HereServiceGrpc.class) {
        if ((getHelloMethod = HereServiceGrpc.getHelloMethod) == null) {
          HereServiceGrpc.getHelloMethod = getHelloMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.iamhere.Grpc.HelloRequest, edu.sjsu.cs249.iamhere.Grpc.HelloResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "hello"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.iamhere.Grpc.HelloRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.iamhere.Grpc.HelloResponse.getDefaultInstance()))
              .setSchemaDescriptor(new HereServiceMethodDescriptorSupplier("hello"))
              .build();
        }
      }
    }
    return getHelloMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static HereServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HereServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HereServiceStub>() {
        @java.lang.Override
        public HereServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HereServiceStub(channel, callOptions);
        }
      };
    return HereServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static HereServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HereServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HereServiceBlockingStub>() {
        @java.lang.Override
        public HereServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HereServiceBlockingStub(channel, callOptions);
        }
      };
    return HereServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static HereServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<HereServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<HereServiceFutureStub>() {
        @java.lang.Override
        public HereServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new HereServiceFutureStub(channel, callOptions);
        }
      };
    return HereServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class HereServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void here(edu.sjsu.cs249.iamhere.Grpc.HereRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HereResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHereMethod(), responseObserver);
    }

    /**
     */
    public void hello(edu.sjsu.cs249.iamhere.Grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getHelloMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getHereMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.iamhere.Grpc.HereRequest,
                edu.sjsu.cs249.iamhere.Grpc.HereResponse>(
                  this, METHODID_HERE)))
          .addMethod(
            getHelloMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.iamhere.Grpc.HelloRequest,
                edu.sjsu.cs249.iamhere.Grpc.HelloResponse>(
                  this, METHODID_HELLO)))
          .build();
    }
  }

  /**
   */
  public static final class HereServiceStub extends io.grpc.stub.AbstractAsyncStub<HereServiceStub> {
    private HereServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HereServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HereServiceStub(channel, callOptions);
    }

    /**
     */
    public void here(edu.sjsu.cs249.iamhere.Grpc.HereRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HereResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHereMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void hello(edu.sjsu.cs249.iamhere.Grpc.HelloRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HelloResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class HereServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<HereServiceBlockingStub> {
    private HereServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HereServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HereServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.sjsu.cs249.iamhere.Grpc.HereResponse here(edu.sjsu.cs249.iamhere.Grpc.HereRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHereMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.sjsu.cs249.iamhere.Grpc.HelloResponse hello(edu.sjsu.cs249.iamhere.Grpc.HelloRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getHelloMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class HereServiceFutureStub extends io.grpc.stub.AbstractFutureStub<HereServiceFutureStub> {
    private HereServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected HereServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new HereServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.iamhere.Grpc.HereResponse> here(
        edu.sjsu.cs249.iamhere.Grpc.HereRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHereMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.iamhere.Grpc.HelloResponse> hello(
        edu.sjsu.cs249.iamhere.Grpc.HelloRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getHelloMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_HERE = 0;
  private static final int METHODID_HELLO = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final HereServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(HereServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_HERE:
          serviceImpl.here((edu.sjsu.cs249.iamhere.Grpc.HereRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HereResponse>) responseObserver);
          break;
        case METHODID_HELLO:
          serviceImpl.hello((edu.sjsu.cs249.iamhere.Grpc.HelloRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.iamhere.Grpc.HelloResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class HereServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    HereServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.sjsu.cs249.iamhere.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("HereService");
    }
  }

  private static final class HereServiceFileDescriptorSupplier
      extends HereServiceBaseDescriptorSupplier {
    HereServiceFileDescriptorSupplier() {}
  }

  private static final class HereServiceMethodDescriptorSupplier
      extends HereServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    HereServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (HereServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new HereServiceFileDescriptorSupplier())
              .addMethod(getHereMethod())
              .addMethod(getHelloMethod())
              .build();
        }
      }
    }
    return result;
  }
}
