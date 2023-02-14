package edu.sjsu.cs249.abd;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.53.0)",
    comments = "Source: grpc.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ABDServiceGrpc {

  private ABDServiceGrpc() {}

  public static final String SERVICE_NAME = "abd.ABDService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.NameRequest,
      edu.sjsu.cs249.abd.Grpc.NameResponse> getNameMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "name",
      requestType = edu.sjsu.cs249.abd.Grpc.NameRequest.class,
      responseType = edu.sjsu.cs249.abd.Grpc.NameResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.NameRequest,
      edu.sjsu.cs249.abd.Grpc.NameResponse> getNameMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.NameRequest, edu.sjsu.cs249.abd.Grpc.NameResponse> getNameMethod;
    if ((getNameMethod = ABDServiceGrpc.getNameMethod) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getNameMethod = ABDServiceGrpc.getNameMethod) == null) {
          ABDServiceGrpc.getNameMethod = getNameMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.NameRequest, edu.sjsu.cs249.abd.Grpc.NameResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "name"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.NameRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.NameResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("name"))
              .build();
        }
      }
    }
    return getNameMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.EnableRequest,
      edu.sjsu.cs249.abd.Grpc.EnableResponse> getEnableRequestsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "enableRequests",
      requestType = edu.sjsu.cs249.abd.Grpc.EnableRequest.class,
      responseType = edu.sjsu.cs249.abd.Grpc.EnableResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.EnableRequest,
      edu.sjsu.cs249.abd.Grpc.EnableResponse> getEnableRequestsMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.EnableRequest, edu.sjsu.cs249.abd.Grpc.EnableResponse> getEnableRequestsMethod;
    if ((getEnableRequestsMethod = ABDServiceGrpc.getEnableRequestsMethod) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getEnableRequestsMethod = ABDServiceGrpc.getEnableRequestsMethod) == null) {
          ABDServiceGrpc.getEnableRequestsMethod = getEnableRequestsMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.EnableRequest, edu.sjsu.cs249.abd.Grpc.EnableResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "enableRequests"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.EnableRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.EnableResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("enableRequests"))
              .build();
        }
      }
    }
    return getEnableRequestsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read1Request,
      edu.sjsu.cs249.abd.Grpc.Read1Response> getRead1Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "read1",
      requestType = edu.sjsu.cs249.abd.Grpc.Read1Request.class,
      responseType = edu.sjsu.cs249.abd.Grpc.Read1Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read1Request,
      edu.sjsu.cs249.abd.Grpc.Read1Response> getRead1Method() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read1Request, edu.sjsu.cs249.abd.Grpc.Read1Response> getRead1Method;
    if ((getRead1Method = ABDServiceGrpc.getRead1Method) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getRead1Method = ABDServiceGrpc.getRead1Method) == null) {
          ABDServiceGrpc.getRead1Method = getRead1Method =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.Read1Request, edu.sjsu.cs249.abd.Grpc.Read1Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "read1"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.Read1Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.Read1Response.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("read1"))
              .build();
        }
      }
    }
    return getRead1Method;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read2Request,
      edu.sjsu.cs249.abd.Grpc.Read2Response> getRead2Method;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "read2",
      requestType = edu.sjsu.cs249.abd.Grpc.Read2Request.class,
      responseType = edu.sjsu.cs249.abd.Grpc.Read2Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read2Request,
      edu.sjsu.cs249.abd.Grpc.Read2Response> getRead2Method() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.Read2Request, edu.sjsu.cs249.abd.Grpc.Read2Response> getRead2Method;
    if ((getRead2Method = ABDServiceGrpc.getRead2Method) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getRead2Method = ABDServiceGrpc.getRead2Method) == null) {
          ABDServiceGrpc.getRead2Method = getRead2Method =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.Read2Request, edu.sjsu.cs249.abd.Grpc.Read2Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "read2"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.Read2Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.Read2Response.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("read2"))
              .build();
        }
      }
    }
    return getRead2Method;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.WriteRequest,
      edu.sjsu.cs249.abd.Grpc.WriteResponse> getWriteMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "write",
      requestType = edu.sjsu.cs249.abd.Grpc.WriteRequest.class,
      responseType = edu.sjsu.cs249.abd.Grpc.WriteResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.WriteRequest,
      edu.sjsu.cs249.abd.Grpc.WriteResponse> getWriteMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.WriteRequest, edu.sjsu.cs249.abd.Grpc.WriteResponse> getWriteMethod;
    if ((getWriteMethod = ABDServiceGrpc.getWriteMethod) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getWriteMethod = ABDServiceGrpc.getWriteMethod) == null) {
          ABDServiceGrpc.getWriteMethod = getWriteMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.WriteRequest, edu.sjsu.cs249.abd.Grpc.WriteResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "write"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.WriteRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.WriteResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("write"))
              .build();
        }
      }
    }
    return getWriteMethod;
  }

  private static volatile io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.ExitRequest,
      edu.sjsu.cs249.abd.Grpc.ExitResponse> getExitMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "exit",
      requestType = edu.sjsu.cs249.abd.Grpc.ExitRequest.class,
      responseType = edu.sjsu.cs249.abd.Grpc.ExitResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.ExitRequest,
      edu.sjsu.cs249.abd.Grpc.ExitResponse> getExitMethod() {
    io.grpc.MethodDescriptor<edu.sjsu.cs249.abd.Grpc.ExitRequest, edu.sjsu.cs249.abd.Grpc.ExitResponse> getExitMethod;
    if ((getExitMethod = ABDServiceGrpc.getExitMethod) == null) {
      synchronized (ABDServiceGrpc.class) {
        if ((getExitMethod = ABDServiceGrpc.getExitMethod) == null) {
          ABDServiceGrpc.getExitMethod = getExitMethod =
              io.grpc.MethodDescriptor.<edu.sjsu.cs249.abd.Grpc.ExitRequest, edu.sjsu.cs249.abd.Grpc.ExitResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "exit"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.ExitRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  edu.sjsu.cs249.abd.Grpc.ExitResponse.getDefaultInstance()))
              .setSchemaDescriptor(new ABDServiceMethodDescriptorSupplier("exit"))
              .build();
        }
      }
    }
    return getExitMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ABDServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ABDServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ABDServiceStub>() {
        @java.lang.Override
        public ABDServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ABDServiceStub(channel, callOptions);
        }
      };
    return ABDServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ABDServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ABDServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ABDServiceBlockingStub>() {
        @java.lang.Override
        public ABDServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ABDServiceBlockingStub(channel, callOptions);
        }
      };
    return ABDServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ABDServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ABDServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ABDServiceFutureStub>() {
        @java.lang.Override
        public ABDServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ABDServiceFutureStub(channel, callOptions);
        }
      };
    return ABDServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class ABDServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void name(edu.sjsu.cs249.abd.Grpc.NameRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.NameResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getNameMethod(), responseObserver);
    }

    /**
     */
    public void enableRequests(edu.sjsu.cs249.abd.Grpc.EnableRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.EnableResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getEnableRequestsMethod(), responseObserver);
    }

    /**
     */
    public void read1(edu.sjsu.cs249.abd.Grpc.Read1Request request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read1Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRead1Method(), responseObserver);
    }

    /**
     */
    public void read2(edu.sjsu.cs249.abd.Grpc.Read2Request request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read2Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getRead2Method(), responseObserver);
    }

    /**
     */
    public void write(edu.sjsu.cs249.abd.Grpc.WriteRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.WriteResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getWriteMethod(), responseObserver);
    }

    /**
     * <pre>
     * causes the server to immediately exit
     * </pre>
     */
    public void exit(edu.sjsu.cs249.abd.Grpc.ExitRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.ExitResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getExitMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getNameMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.NameRequest,
                edu.sjsu.cs249.abd.Grpc.NameResponse>(
                  this, METHODID_NAME)))
          .addMethod(
            getEnableRequestsMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.EnableRequest,
                edu.sjsu.cs249.abd.Grpc.EnableResponse>(
                  this, METHODID_ENABLE_REQUESTS)))
          .addMethod(
            getRead1Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.Read1Request,
                edu.sjsu.cs249.abd.Grpc.Read1Response>(
                  this, METHODID_READ1)))
          .addMethod(
            getRead2Method(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.Read2Request,
                edu.sjsu.cs249.abd.Grpc.Read2Response>(
                  this, METHODID_READ2)))
          .addMethod(
            getWriteMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.WriteRequest,
                edu.sjsu.cs249.abd.Grpc.WriteResponse>(
                  this, METHODID_WRITE)))
          .addMethod(
            getExitMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                edu.sjsu.cs249.abd.Grpc.ExitRequest,
                edu.sjsu.cs249.abd.Grpc.ExitResponse>(
                  this, METHODID_EXIT)))
          .build();
    }
  }

  /**
   */
  public static final class ABDServiceStub extends io.grpc.stub.AbstractAsyncStub<ABDServiceStub> {
    private ABDServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ABDServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ABDServiceStub(channel, callOptions);
    }

    /**
     */
    public void name(edu.sjsu.cs249.abd.Grpc.NameRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.NameResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getNameMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void enableRequests(edu.sjsu.cs249.abd.Grpc.EnableRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.EnableResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getEnableRequestsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void read1(edu.sjsu.cs249.abd.Grpc.Read1Request request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read1Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRead1Method(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void read2(edu.sjsu.cs249.abd.Grpc.Read2Request request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read2Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getRead2Method(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void write(edu.sjsu.cs249.abd.Grpc.WriteRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.WriteResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * causes the server to immediately exit
     * </pre>
     */
    public void exit(edu.sjsu.cs249.abd.Grpc.ExitRequest request,
        io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.ExitResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getExitMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class ABDServiceBlockingStub extends io.grpc.stub.AbstractBlockingStub<ABDServiceBlockingStub> {
    private ABDServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ABDServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ABDServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public edu.sjsu.cs249.abd.Grpc.NameResponse name(edu.sjsu.cs249.abd.Grpc.NameRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getNameMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.sjsu.cs249.abd.Grpc.EnableResponse enableRequests(edu.sjsu.cs249.abd.Grpc.EnableRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getEnableRequestsMethod(), getCallOptions(), request);
    }

    /**
     */
    public edu.sjsu.cs249.abd.Grpc.Read1Response read1(edu.sjsu.cs249.abd.Grpc.Read1Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRead1Method(), getCallOptions(), request);
    }

    /**
     */
    public edu.sjsu.cs249.abd.Grpc.Read2Response read2(edu.sjsu.cs249.abd.Grpc.Read2Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getRead2Method(), getCallOptions(), request);
    }

    /**
     */
    public edu.sjsu.cs249.abd.Grpc.WriteResponse write(edu.sjsu.cs249.abd.Grpc.WriteRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getWriteMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * causes the server to immediately exit
     * </pre>
     */
    public edu.sjsu.cs249.abd.Grpc.ExitResponse exit(edu.sjsu.cs249.abd.Grpc.ExitRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getExitMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class ABDServiceFutureStub extends io.grpc.stub.AbstractFutureStub<ABDServiceFutureStub> {
    private ABDServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ABDServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ABDServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.NameResponse> name(
        edu.sjsu.cs249.abd.Grpc.NameRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getNameMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.EnableResponse> enableRequests(
        edu.sjsu.cs249.abd.Grpc.EnableRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getEnableRequestsMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.Read1Response> read1(
        edu.sjsu.cs249.abd.Grpc.Read1Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRead1Method(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.Read2Response> read2(
        edu.sjsu.cs249.abd.Grpc.Read2Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getRead2Method(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.WriteResponse> write(
        edu.sjsu.cs249.abd.Grpc.WriteRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getWriteMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * causes the server to immediately exit
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<edu.sjsu.cs249.abd.Grpc.ExitResponse> exit(
        edu.sjsu.cs249.abd.Grpc.ExitRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getExitMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_NAME = 0;
  private static final int METHODID_ENABLE_REQUESTS = 1;
  private static final int METHODID_READ1 = 2;
  private static final int METHODID_READ2 = 3;
  private static final int METHODID_WRITE = 4;
  private static final int METHODID_EXIT = 5;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final ABDServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(ABDServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_NAME:
          serviceImpl.name((edu.sjsu.cs249.abd.Grpc.NameRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.NameResponse>) responseObserver);
          break;
        case METHODID_ENABLE_REQUESTS:
          serviceImpl.enableRequests((edu.sjsu.cs249.abd.Grpc.EnableRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.EnableResponse>) responseObserver);
          break;
        case METHODID_READ1:
          serviceImpl.read1((edu.sjsu.cs249.abd.Grpc.Read1Request) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read1Response>) responseObserver);
          break;
        case METHODID_READ2:
          serviceImpl.read2((edu.sjsu.cs249.abd.Grpc.Read2Request) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.Read2Response>) responseObserver);
          break;
        case METHODID_WRITE:
          serviceImpl.write((edu.sjsu.cs249.abd.Grpc.WriteRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.WriteResponse>) responseObserver);
          break;
        case METHODID_EXIT:
          serviceImpl.exit((edu.sjsu.cs249.abd.Grpc.ExitRequest) request,
              (io.grpc.stub.StreamObserver<edu.sjsu.cs249.abd.Grpc.ExitResponse>) responseObserver);
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

  private static abstract class ABDServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ABDServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return edu.sjsu.cs249.abd.Grpc.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ABDService");
    }
  }

  private static final class ABDServiceFileDescriptorSupplier
      extends ABDServiceBaseDescriptorSupplier {
    ABDServiceFileDescriptorSupplier() {}
  }

  private static final class ABDServiceMethodDescriptorSupplier
      extends ABDServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    ABDServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (ABDServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ABDServiceFileDescriptorSupplier())
              .addMethod(getNameMethod())
              .addMethod(getEnableRequestsMethod())
              .addMethod(getRead1Method())
              .addMethod(getRead2Method())
              .addMethod(getWriteMethod())
              .addMethod(getExitMethod())
              .build();
        }
      }
    }
    return result;
  }
}
