package node.communication.base;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.24.0)",
    comments = "Source: NodeCommunicationServer.proto")
public final class NodeCommunicationGrpc {

  private NodeCommunicationGrpc() {}

  public static final String SERVICE_NAME = "node.communication.base.NodeCommunication";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.TestRequest,
      node.communication.base.NodeCommunicationServer.TestResponse> getTestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "test",
      requestType = node.communication.base.NodeCommunicationServer.TestRequest.class,
      responseType = node.communication.base.NodeCommunicationServer.TestResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.TestRequest,
      node.communication.base.NodeCommunicationServer.TestResponse> getTestMethod() {
    io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.TestRequest, node.communication.base.NodeCommunicationServer.TestResponse> getTestMethod;
    if ((getTestMethod = NodeCommunicationGrpc.getTestMethod) == null) {
      synchronized (NodeCommunicationGrpc.class) {
        if ((getTestMethod = NodeCommunicationGrpc.getTestMethod) == null) {
          NodeCommunicationGrpc.getTestMethod = getTestMethod =
              io.grpc.MethodDescriptor.<node.communication.base.NodeCommunicationServer.TestRequest, node.communication.base.NodeCommunicationServer.TestResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "test"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.TestRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.TestResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeCommunicationMethodDescriptorSupplier("test"))
              .build();
        }
      }
    }
    return getTestMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.PingRequest,
      node.communication.base.NodeCommunicationServer.PingResponse> getPingMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ping",
      requestType = node.communication.base.NodeCommunicationServer.PingRequest.class,
      responseType = node.communication.base.NodeCommunicationServer.PingResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.PingRequest,
      node.communication.base.NodeCommunicationServer.PingResponse> getPingMethod() {
    io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.PingRequest, node.communication.base.NodeCommunicationServer.PingResponse> getPingMethod;
    if ((getPingMethod = NodeCommunicationGrpc.getPingMethod) == null) {
      synchronized (NodeCommunicationGrpc.class) {
        if ((getPingMethod = NodeCommunicationGrpc.getPingMethod) == null) {
          NodeCommunicationGrpc.getPingMethod = getPingMethod =
              io.grpc.MethodDescriptor.<node.communication.base.NodeCommunicationServer.PingRequest, node.communication.base.NodeCommunicationServer.PingResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ping"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.PingRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.PingResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeCommunicationMethodDescriptorSupplier("ping"))
              .build();
        }
      }
    }
    return getPingMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.DownloadRequest,
      node.communication.base.NodeCommunicationServer.DownloadResponse> getDownloadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "download",
      requestType = node.communication.base.NodeCommunicationServer.DownloadRequest.class,
      responseType = node.communication.base.NodeCommunicationServer.DownloadResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.DownloadRequest,
      node.communication.base.NodeCommunicationServer.DownloadResponse> getDownloadMethod() {
    io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.DownloadRequest, node.communication.base.NodeCommunicationServer.DownloadResponse> getDownloadMethod;
    if ((getDownloadMethod = NodeCommunicationGrpc.getDownloadMethod) == null) {
      synchronized (NodeCommunicationGrpc.class) {
        if ((getDownloadMethod = NodeCommunicationGrpc.getDownloadMethod) == null) {
          NodeCommunicationGrpc.getDownloadMethod = getDownloadMethod =
              io.grpc.MethodDescriptor.<node.communication.base.NodeCommunicationServer.DownloadRequest, node.communication.base.NodeCommunicationServer.DownloadResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "download"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.DownloadRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.DownloadResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeCommunicationMethodDescriptorSupplier("download"))
              .build();
        }
      }
    }
    return getDownloadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.IsUpdateRequest,
      node.communication.base.NodeCommunicationServer.IsUpdateResponse> getIsUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "isUpdate",
      requestType = node.communication.base.NodeCommunicationServer.IsUpdateRequest.class,
      responseType = node.communication.base.NodeCommunicationServer.IsUpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.IsUpdateRequest,
      node.communication.base.NodeCommunicationServer.IsUpdateResponse> getIsUpdateMethod() {
    io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.IsUpdateRequest, node.communication.base.NodeCommunicationServer.IsUpdateResponse> getIsUpdateMethod;
    if ((getIsUpdateMethod = NodeCommunicationGrpc.getIsUpdateMethod) == null) {
      synchronized (NodeCommunicationGrpc.class) {
        if ((getIsUpdateMethod = NodeCommunicationGrpc.getIsUpdateMethod) == null) {
          NodeCommunicationGrpc.getIsUpdateMethod = getIsUpdateMethod =
              io.grpc.MethodDescriptor.<node.communication.base.NodeCommunicationServer.IsUpdateRequest, node.communication.base.NodeCommunicationServer.IsUpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "isUpdate"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.IsUpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.IsUpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeCommunicationMethodDescriptorSupplier("isUpdate"))
              .build();
        }
      }
    }
    return getIsUpdateMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.UpdateRequest,
      node.communication.base.NodeCommunicationServer.UpdateResponse> getUpdateMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "update",
      requestType = node.communication.base.NodeCommunicationServer.UpdateRequest.class,
      responseType = node.communication.base.NodeCommunicationServer.UpdateResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.UpdateRequest,
      node.communication.base.NodeCommunicationServer.UpdateResponse> getUpdateMethod() {
    io.grpc.MethodDescriptor<node.communication.base.NodeCommunicationServer.UpdateRequest, node.communication.base.NodeCommunicationServer.UpdateResponse> getUpdateMethod;
    if ((getUpdateMethod = NodeCommunicationGrpc.getUpdateMethod) == null) {
      synchronized (NodeCommunicationGrpc.class) {
        if ((getUpdateMethod = NodeCommunicationGrpc.getUpdateMethod) == null) {
          NodeCommunicationGrpc.getUpdateMethod = getUpdateMethod =
              io.grpc.MethodDescriptor.<node.communication.base.NodeCommunicationServer.UpdateRequest, node.communication.base.NodeCommunicationServer.UpdateResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "update"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.UpdateRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.communication.base.NodeCommunicationServer.UpdateResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeCommunicationMethodDescriptorSupplier("update"))
              .build();
        }
      }
    }
    return getUpdateMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeCommunicationStub newStub(io.grpc.Channel channel) {
    return new NodeCommunicationStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeCommunicationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NodeCommunicationBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeCommunicationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NodeCommunicationFutureStub(channel);
  }

  /**
   */
  public static abstract class NodeCommunicationImplBase implements io.grpc.BindableService {

    /**
     */
    public void test(node.communication.base.NodeCommunicationServer.TestRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.TestResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getTestMethod(), responseObserver);
    }

    /**
     */
    public void ping(node.communication.base.NodeCommunicationServer.PingRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.PingResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getPingMethod(), responseObserver);
    }

    /**
     */
    public void download(node.communication.base.NodeCommunicationServer.DownloadRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.DownloadResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getDownloadMethod(), responseObserver);
    }

    /**
     */
    public void isUpdate(node.communication.base.NodeCommunicationServer.IsUpdateRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.IsUpdateResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getIsUpdateMethod(), responseObserver);
    }

    /**
     */
    public void update(node.communication.base.NodeCommunicationServer.UpdateRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.UpdateResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getUpdateMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getTestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.communication.base.NodeCommunicationServer.TestRequest,
                node.communication.base.NodeCommunicationServer.TestResponse>(
                  this, METHODID_TEST)))
          .addMethod(
            getPingMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.communication.base.NodeCommunicationServer.PingRequest,
                node.communication.base.NodeCommunicationServer.PingResponse>(
                  this, METHODID_PING)))
          .addMethod(
            getDownloadMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.communication.base.NodeCommunicationServer.DownloadRequest,
                node.communication.base.NodeCommunicationServer.DownloadResponse>(
                  this, METHODID_DOWNLOAD)))
          .addMethod(
            getIsUpdateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.communication.base.NodeCommunicationServer.IsUpdateRequest,
                node.communication.base.NodeCommunicationServer.IsUpdateResponse>(
                  this, METHODID_IS_UPDATE)))
          .addMethod(
            getUpdateMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.communication.base.NodeCommunicationServer.UpdateRequest,
                node.communication.base.NodeCommunicationServer.UpdateResponse>(
                  this, METHODID_UPDATE)))
          .build();
    }
  }

  /**
   */
  public static final class NodeCommunicationStub extends io.grpc.stub.AbstractStub<NodeCommunicationStub> {
    private NodeCommunicationStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeCommunicationStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeCommunicationStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeCommunicationStub(channel, callOptions);
    }

    /**
     */
    public void test(node.communication.base.NodeCommunicationServer.TestRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.TestResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getTestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void ping(node.communication.base.NodeCommunicationServer.PingRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.PingResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void download(node.communication.base.NodeCommunicationServer.DownloadRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.DownloadResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getDownloadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void isUpdate(node.communication.base.NodeCommunicationServer.IsUpdateRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.IsUpdateResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getIsUpdateMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void update(node.communication.base.NodeCommunicationServer.UpdateRequest request,
        io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.UpdateResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeCommunicationBlockingStub extends io.grpc.stub.AbstractStub<NodeCommunicationBlockingStub> {
    private NodeCommunicationBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeCommunicationBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeCommunicationBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeCommunicationBlockingStub(channel, callOptions);
    }

    /**
     */
    public node.communication.base.NodeCommunicationServer.TestResponse test(node.communication.base.NodeCommunicationServer.TestRequest request) {
      return blockingUnaryCall(
          getChannel(), getTestMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.communication.base.NodeCommunicationServer.PingResponse ping(node.communication.base.NodeCommunicationServer.PingRequest request) {
      return blockingUnaryCall(
          getChannel(), getPingMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.communication.base.NodeCommunicationServer.DownloadResponse download(node.communication.base.NodeCommunicationServer.DownloadRequest request) {
      return blockingUnaryCall(
          getChannel(), getDownloadMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.communication.base.NodeCommunicationServer.IsUpdateResponse isUpdate(node.communication.base.NodeCommunicationServer.IsUpdateRequest request) {
      return blockingUnaryCall(
          getChannel(), getIsUpdateMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.communication.base.NodeCommunicationServer.UpdateResponse update(node.communication.base.NodeCommunicationServer.UpdateRequest request) {
      return blockingUnaryCall(
          getChannel(), getUpdateMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeCommunicationFutureStub extends io.grpc.stub.AbstractStub<NodeCommunicationFutureStub> {
    private NodeCommunicationFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeCommunicationFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeCommunicationFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeCommunicationFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.communication.base.NodeCommunicationServer.TestResponse> test(
        node.communication.base.NodeCommunicationServer.TestRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getTestMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.communication.base.NodeCommunicationServer.PingResponse> ping(
        node.communication.base.NodeCommunicationServer.PingRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getPingMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.communication.base.NodeCommunicationServer.DownloadResponse> download(
        node.communication.base.NodeCommunicationServer.DownloadRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getDownloadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.communication.base.NodeCommunicationServer.IsUpdateResponse> isUpdate(
        node.communication.base.NodeCommunicationServer.IsUpdateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getIsUpdateMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.communication.base.NodeCommunicationServer.UpdateResponse> update(
        node.communication.base.NodeCommunicationServer.UpdateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getUpdateMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_TEST = 0;
  private static final int METHODID_PING = 1;
  private static final int METHODID_DOWNLOAD = 2;
  private static final int METHODID_IS_UPDATE = 3;
  private static final int METHODID_UPDATE = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeCommunicationImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeCommunicationImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_TEST:
          serviceImpl.test((node.communication.base.NodeCommunicationServer.TestRequest) request,
              (io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.TestResponse>) responseObserver);
          break;
        case METHODID_PING:
          serviceImpl.ping((node.communication.base.NodeCommunicationServer.PingRequest) request,
              (io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.PingResponse>) responseObserver);
          break;
        case METHODID_DOWNLOAD:
          serviceImpl.download((node.communication.base.NodeCommunicationServer.DownloadRequest) request,
              (io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.DownloadResponse>) responseObserver);
          break;
        case METHODID_IS_UPDATE:
          serviceImpl.isUpdate((node.communication.base.NodeCommunicationServer.IsUpdateRequest) request,
              (io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.IsUpdateResponse>) responseObserver);
          break;
        case METHODID_UPDATE:
          serviceImpl.update((node.communication.base.NodeCommunicationServer.UpdateRequest) request,
              (io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.UpdateResponse>) responseObserver);
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

  private static abstract class NodeCommunicationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeCommunicationBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return node.communication.base.NodeCommunicationServer.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NodeCommunication");
    }
  }

  private static final class NodeCommunicationFileDescriptorSupplier
      extends NodeCommunicationBaseDescriptorSupplier {
    NodeCommunicationFileDescriptorSupplier() {}
  }

  private static final class NodeCommunicationMethodDescriptorSupplier
      extends NodeCommunicationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeCommunicationMethodDescriptorSupplier(String methodName) {
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
      synchronized (NodeCommunicationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeCommunicationFileDescriptorSupplier())
              .addMethod(getTestMethod())
              .addMethod(getPingMethod())
              .addMethod(getDownloadMethod())
              .addMethod(getIsUpdateMethod())
              .addMethod(getUpdateMethod())
              .build();
        }
      }
    }
    return result;
  }
}
