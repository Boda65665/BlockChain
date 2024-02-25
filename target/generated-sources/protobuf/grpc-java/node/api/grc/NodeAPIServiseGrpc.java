package node.api.grc;

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
    comments = "Source: NodeApi.proto")
public final class NodeAPIServiseGrpc {

  private NodeAPIServiseGrpc() {}

  public static final String SERVICE_NAME = "node.api.grc.NodeAPIServise";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetInfoBlockChainRequest,
      node.api.grc.NodeApi.GetInfoBlockChainResponse> getGetBlockChainInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getBlockChainInfo",
      requestType = node.api.grc.NodeApi.GetInfoBlockChainRequest.class,
      responseType = node.api.grc.NodeApi.GetInfoBlockChainResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetInfoBlockChainRequest,
      node.api.grc.NodeApi.GetInfoBlockChainResponse> getGetBlockChainInfoMethod() {
    io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetInfoBlockChainRequest, node.api.grc.NodeApi.GetInfoBlockChainResponse> getGetBlockChainInfoMethod;
    if ((getGetBlockChainInfoMethod = NodeAPIServiseGrpc.getGetBlockChainInfoMethod) == null) {
      synchronized (NodeAPIServiseGrpc.class) {
        if ((getGetBlockChainInfoMethod = NodeAPIServiseGrpc.getGetBlockChainInfoMethod) == null) {
          NodeAPIServiseGrpc.getGetBlockChainInfoMethod = getGetBlockChainInfoMethod =
              io.grpc.MethodDescriptor.<node.api.grc.NodeApi.GetInfoBlockChainRequest, node.api.grc.NodeApi.GetInfoBlockChainResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getBlockChainInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetInfoBlockChainRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetInfoBlockChainResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeAPIServiseMethodDescriptorSupplier("getBlockChainInfo"))
              .build();
        }
      }
    }
    return getGetBlockChainInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.api.grc.NodeApi.SendTransactionRequest,
      node.api.grc.NodeApi.SendTransactionResponse> getSendTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "sendTransaction",
      requestType = node.api.grc.NodeApi.SendTransactionRequest.class,
      responseType = node.api.grc.NodeApi.SendTransactionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.api.grc.NodeApi.SendTransactionRequest,
      node.api.grc.NodeApi.SendTransactionResponse> getSendTransactionMethod() {
    io.grpc.MethodDescriptor<node.api.grc.NodeApi.SendTransactionRequest, node.api.grc.NodeApi.SendTransactionResponse> getSendTransactionMethod;
    if ((getSendTransactionMethod = NodeAPIServiseGrpc.getSendTransactionMethod) == null) {
      synchronized (NodeAPIServiseGrpc.class) {
        if ((getSendTransactionMethod = NodeAPIServiseGrpc.getSendTransactionMethod) == null) {
          NodeAPIServiseGrpc.getSendTransactionMethod = getSendTransactionMethod =
              io.grpc.MethodDescriptor.<node.api.grc.NodeApi.SendTransactionRequest, node.api.grc.NodeApi.SendTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "sendTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.SendTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.SendTransactionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeAPIServiseMethodDescriptorSupplier("sendTransaction"))
              .build();
        }
      }
    }
    return getSendTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetTransactionRequest,
      node.api.grc.NodeApi.GetTransactionResponse> getGetTransactionMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getTransaction",
      requestType = node.api.grc.NodeApi.GetTransactionRequest.class,
      responseType = node.api.grc.NodeApi.GetTransactionResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetTransactionRequest,
      node.api.grc.NodeApi.GetTransactionResponse> getGetTransactionMethod() {
    io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetTransactionRequest, node.api.grc.NodeApi.GetTransactionResponse> getGetTransactionMethod;
    if ((getGetTransactionMethod = NodeAPIServiseGrpc.getGetTransactionMethod) == null) {
      synchronized (NodeAPIServiseGrpc.class) {
        if ((getGetTransactionMethod = NodeAPIServiseGrpc.getGetTransactionMethod) == null) {
          NodeAPIServiseGrpc.getGetTransactionMethod = getGetTransactionMethod =
              io.grpc.MethodDescriptor.<node.api.grc.NodeApi.GetTransactionRequest, node.api.grc.NodeApi.GetTransactionResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getTransaction"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetTransactionRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetTransactionResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeAPIServiseMethodDescriptorSupplier("getTransaction"))
              .build();
        }
      }
    }
    return getGetTransactionMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBlockRequest,
      node.api.grc.NodeApi.GetBlockResponse> getGetBlockMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getBlock",
      requestType = node.api.grc.NodeApi.GetBlockRequest.class,
      responseType = node.api.grc.NodeApi.GetBlockResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBlockRequest,
      node.api.grc.NodeApi.GetBlockResponse> getGetBlockMethod() {
    io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBlockRequest, node.api.grc.NodeApi.GetBlockResponse> getGetBlockMethod;
    if ((getGetBlockMethod = NodeAPIServiseGrpc.getGetBlockMethod) == null) {
      synchronized (NodeAPIServiseGrpc.class) {
        if ((getGetBlockMethod = NodeAPIServiseGrpc.getGetBlockMethod) == null) {
          NodeAPIServiseGrpc.getGetBlockMethod = getGetBlockMethod =
              io.grpc.MethodDescriptor.<node.api.grc.NodeApi.GetBlockRequest, node.api.grc.NodeApi.GetBlockResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getBlock"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetBlockRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetBlockResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeAPIServiseMethodDescriptorSupplier("getBlock"))
              .build();
        }
      }
    }
    return getGetBlockMethod;
  }

  private static volatile io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBalanceRequest,
      node.api.grc.NodeApi.GetBalanceResponse> getGetBalanceMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getBalance",
      requestType = node.api.grc.NodeApi.GetBalanceRequest.class,
      responseType = node.api.grc.NodeApi.GetBalanceResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBalanceRequest,
      node.api.grc.NodeApi.GetBalanceResponse> getGetBalanceMethod() {
    io.grpc.MethodDescriptor<node.api.grc.NodeApi.GetBalanceRequest, node.api.grc.NodeApi.GetBalanceResponse> getGetBalanceMethod;
    if ((getGetBalanceMethod = NodeAPIServiseGrpc.getGetBalanceMethod) == null) {
      synchronized (NodeAPIServiseGrpc.class) {
        if ((getGetBalanceMethod = NodeAPIServiseGrpc.getGetBalanceMethod) == null) {
          NodeAPIServiseGrpc.getGetBalanceMethod = getGetBalanceMethod =
              io.grpc.MethodDescriptor.<node.api.grc.NodeApi.GetBalanceRequest, node.api.grc.NodeApi.GetBalanceResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getBalance"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetBalanceRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  node.api.grc.NodeApi.GetBalanceResponse.getDefaultInstance()))
              .setSchemaDescriptor(new NodeAPIServiseMethodDescriptorSupplier("getBalance"))
              .build();
        }
      }
    }
    return getGetBalanceMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NodeAPIServiseStub newStub(io.grpc.Channel channel) {
    return new NodeAPIServiseStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NodeAPIServiseBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new NodeAPIServiseBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NodeAPIServiseFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new NodeAPIServiseFutureStub(channel);
  }

  /**
   */
  public static abstract class NodeAPIServiseImplBase implements io.grpc.BindableService {

    /**
     */
    public void getBlockChainInfo(node.api.grc.NodeApi.GetInfoBlockChainRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetInfoBlockChainResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBlockChainInfoMethod(), responseObserver);
    }

    /**
     */
    public void sendTransaction(node.api.grc.NodeApi.SendTransactionRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.SendTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getSendTransactionMethod(), responseObserver);
    }

    /**
     */
    public void getTransaction(node.api.grc.NodeApi.GetTransactionRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetTransactionResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetTransactionMethod(), responseObserver);
    }

    /**
     */
    public void getBlock(node.api.grc.NodeApi.GetBlockRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBlockResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBlockMethod(), responseObserver);
    }

    /**
     */
    public void getBalance(node.api.grc.NodeApi.GetBalanceRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBalanceResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetBalanceMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetBlockChainInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.api.grc.NodeApi.GetInfoBlockChainRequest,
                node.api.grc.NodeApi.GetInfoBlockChainResponse>(
                  this, METHODID_GET_BLOCK_CHAIN_INFO)))
          .addMethod(
            getSendTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.api.grc.NodeApi.SendTransactionRequest,
                node.api.grc.NodeApi.SendTransactionResponse>(
                  this, METHODID_SEND_TRANSACTION)))
          .addMethod(
            getGetTransactionMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.api.grc.NodeApi.GetTransactionRequest,
                node.api.grc.NodeApi.GetTransactionResponse>(
                  this, METHODID_GET_TRANSACTION)))
          .addMethod(
            getGetBlockMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.api.grc.NodeApi.GetBlockRequest,
                node.api.grc.NodeApi.GetBlockResponse>(
                  this, METHODID_GET_BLOCK)))
          .addMethod(
            getGetBalanceMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                node.api.grc.NodeApi.GetBalanceRequest,
                node.api.grc.NodeApi.GetBalanceResponse>(
                  this, METHODID_GET_BALANCE)))
          .build();
    }
  }

  /**
   */
  public static final class NodeAPIServiseStub extends io.grpc.stub.AbstractStub<NodeAPIServiseStub> {
    private NodeAPIServiseStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeAPIServiseStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeAPIServiseStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeAPIServiseStub(channel, callOptions);
    }

    /**
     */
    public void getBlockChainInfo(node.api.grc.NodeApi.GetInfoBlockChainRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetInfoBlockChainResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBlockChainInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void sendTransaction(node.api.grc.NodeApi.SendTransactionRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.SendTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getTransaction(node.api.grc.NodeApi.GetTransactionRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetTransactionResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetTransactionMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBlock(node.api.grc.NodeApi.GetBlockRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBlockResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBlockMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBalance(node.api.grc.NodeApi.GetBalanceRequest request,
        io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBalanceResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class NodeAPIServiseBlockingStub extends io.grpc.stub.AbstractStub<NodeAPIServiseBlockingStub> {
    private NodeAPIServiseBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeAPIServiseBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeAPIServiseBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeAPIServiseBlockingStub(channel, callOptions);
    }

    /**
     */
    public node.api.grc.NodeApi.GetInfoBlockChainResponse getBlockChainInfo(node.api.grc.NodeApi.GetInfoBlockChainRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetBlockChainInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.api.grc.NodeApi.SendTransactionResponse sendTransaction(node.api.grc.NodeApi.SendTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getSendTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.api.grc.NodeApi.GetTransactionResponse getTransaction(node.api.grc.NodeApi.GetTransactionRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetTransactionMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.api.grc.NodeApi.GetBlockResponse getBlock(node.api.grc.NodeApi.GetBlockRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetBlockMethod(), getCallOptions(), request);
    }

    /**
     */
    public node.api.grc.NodeApi.GetBalanceResponse getBalance(node.api.grc.NodeApi.GetBalanceRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetBalanceMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class NodeAPIServiseFutureStub extends io.grpc.stub.AbstractStub<NodeAPIServiseFutureStub> {
    private NodeAPIServiseFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private NodeAPIServiseFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NodeAPIServiseFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new NodeAPIServiseFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.api.grc.NodeApi.GetInfoBlockChainResponse> getBlockChainInfo(
        node.api.grc.NodeApi.GetInfoBlockChainRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBlockChainInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.api.grc.NodeApi.SendTransactionResponse> sendTransaction(
        node.api.grc.NodeApi.SendTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getSendTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.api.grc.NodeApi.GetTransactionResponse> getTransaction(
        node.api.grc.NodeApi.GetTransactionRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetTransactionMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.api.grc.NodeApi.GetBlockResponse> getBlock(
        node.api.grc.NodeApi.GetBlockRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBlockMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<node.api.grc.NodeApi.GetBalanceResponse> getBalance(
        node.api.grc.NodeApi.GetBalanceRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetBalanceMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_BLOCK_CHAIN_INFO = 0;
  private static final int METHODID_SEND_TRANSACTION = 1;
  private static final int METHODID_GET_TRANSACTION = 2;
  private static final int METHODID_GET_BLOCK = 3;
  private static final int METHODID_GET_BALANCE = 4;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final NodeAPIServiseImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(NodeAPIServiseImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_BLOCK_CHAIN_INFO:
          serviceImpl.getBlockChainInfo((node.api.grc.NodeApi.GetInfoBlockChainRequest) request,
              (io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetInfoBlockChainResponse>) responseObserver);
          break;
        case METHODID_SEND_TRANSACTION:
          serviceImpl.sendTransaction((node.api.grc.NodeApi.SendTransactionRequest) request,
              (io.grpc.stub.StreamObserver<node.api.grc.NodeApi.SendTransactionResponse>) responseObserver);
          break;
        case METHODID_GET_TRANSACTION:
          serviceImpl.getTransaction((node.api.grc.NodeApi.GetTransactionRequest) request,
              (io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetTransactionResponse>) responseObserver);
          break;
        case METHODID_GET_BLOCK:
          serviceImpl.getBlock((node.api.grc.NodeApi.GetBlockRequest) request,
              (io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBlockResponse>) responseObserver);
          break;
        case METHODID_GET_BALANCE:
          serviceImpl.getBalance((node.api.grc.NodeApi.GetBalanceRequest) request,
              (io.grpc.stub.StreamObserver<node.api.grc.NodeApi.GetBalanceResponse>) responseObserver);
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

  private static abstract class NodeAPIServiseBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    NodeAPIServiseBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return node.api.grc.NodeApi.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("NodeAPIServise");
    }
  }

  private static final class NodeAPIServiseFileDescriptorSupplier
      extends NodeAPIServiseBaseDescriptorSupplier {
    NodeAPIServiseFileDescriptorSupplier() {}
  }

  private static final class NodeAPIServiseMethodDescriptorSupplier
      extends NodeAPIServiseBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    NodeAPIServiseMethodDescriptorSupplier(String methodName) {
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
      synchronized (NodeAPIServiseGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new NodeAPIServiseFileDescriptorSupplier())
              .addMethod(getGetBlockChainInfoMethod())
              .addMethod(getSendTransactionMethod())
              .addMethod(getGetTransactionMethod())
              .addMethod(getGetBlockMethod())
              .addMethod(getGetBalanceMethod())
              .build();
        }
      }
    }
    return result;
  }
}
