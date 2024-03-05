package org.example.CustomBlockChain.API.GRPC;
import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import node.api.grc.NodeAPIServiseGrpc;
import node.api.grc.NodeApi;
import node.api.grc.NodeApi.*;
import node.communication.base.NodeCommunicationServer;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
public class NodeAPIGrpcServiseImpl extends NodeAPIServiseGrpc.NodeAPIServiseImplBase {
    private final JavaChainMethodService javaChainMethodService;
    ValidationServiseNodeAPI validationServiseNodeAPI;
    BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    private final ConverterServiseGrpcEntityCustom converterServise = new ConverterServiseGrpcEntityCustom();
    public NodeAPIGrpcServiseImpl(JavaChainMethodService javaChainMethodService) throws SQLException, IOException, ClassNotFoundException {
        this.javaChainMethodService = javaChainMethodService;
        validationServiseNodeAPI = new ValidationServiseNodeAPI(javaChainMethodService);
    }

    @Override
    public void getBalance(GetBalanceRequest request, StreamObserver<GetBalanceResponse> streamObserver)  {
        try {
            Status statusErrorResponse = validationServiseNodeAPI.validGetBalanceRequest(request);
            if (statusErrorResponse != null) {
                    streamObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
                    streamObserver.onCompleted();

            }

            GetBalanceResponse response = GetBalanceResponse.newBuilder()
                    .setBalance(javaChainMethodService.getAddress(request.getAddress()).getBalance()).build();

            streamObserver.onNext(response);
            streamObserver.onCompleted();
        }
        catch (IOException ignored){
        }
    }
    @Override
    public void getBlock(GetBlockRequest request, StreamObserver<GetBlockResponse> getBlockResponseStreamObserver) {
        try {
        Status statusErrorResponse = validationServiseNodeAPI.validGetBlockRequest(request);
        if (statusErrorResponse!=null){
            getBlockResponseStreamObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
            getBlockResponseStreamObserver.onCompleted();
        }
        Block<ArrayList<Transaction>> block = null;
        if (!request.getHash().isEmpty()) {
            block = javaChainMethodService.getBlockByHash(request.getHash());
        }
        else{
            block = javaChainMethodService.getBlockByNumber(request.getNumber());

        }
        GetBlockResponse getBlockResponse = GetBlockResponse.newBuilder().setBlock(converterServise.blockToGrpcBlock(block)).build();
        getBlockResponseStreamObserver.onNext(getBlockResponse);
        getBlockResponseStreamObserver.onCompleted();


    }
        catch (Exception ignored){

        }
    }
    @Override
    public void getTransaction(GetTransactionRequest request, StreamObserver<GetTransactionResponse> getBlockResponseStreamObserver){
        try {
            validationServiseNodeAPI.validGetTransactionRequest(request);
            Transaction transaction = javaChainMethodService.getTransaction(request.getHash());
            GetTransactionResponse response = GetTransactionResponse.newBuilder().setTransaction(converterServise.dataBlockToGrpcData(transaction)).build();
            getBlockResponseStreamObserver.onNext(response);
            getBlockResponseStreamObserver.onCompleted();
        } catch (IOException e) {
        }

    }
    @Override
    public void sendTransaction(SendTransactionRequest request, StreamObserver<SendTransactionResponse> getBlockResponseStreamObserver) {
        try {
            Status statusErrorResponse = validationServiseNodeAPI.validSendTransactionRequest(request);
            if (statusErrorResponse != null) {
                getBlockResponseStreamObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
                getBlockResponseStreamObserver.onCompleted();
            }
            NodeApi.Transaction transactionStruct = request.getTransactionStruct();
            Transaction transaction = Transaction.newTransactionBuilder()
                    .setSing(request.getSign())
                    .setValue(transactionStruct.getValue())
                    .setTo(transactionStruct.getTo())
                    .setFrom(transactionStruct.getFrom())
                    .setData(transactionStruct.getData())
                    .setGas(transactionStruct.getGas())
                    .setNonce(javaChainMethodService.getNoncePending(transactionStruct.getFrom()))
                    .setGasPrice(transactionStruct.getGasPrice()).build();
            String hash = javaChainMethodService.sendTransaction(transaction);
            SendTransactionResponse response = SendTransactionResponse.newBuilder().setHash(hash).build();
            getBlockResponseStreamObserver.onNext(response);
            getBlockResponseStreamObserver.onCompleted();
        }
        catch (Exception err){

        }
    }
    @Override
    public void getBlockChainInfo(GetInfoBlockChainRequest request,StreamObserver<GetInfoBlockChainResponse> responseObserver) {
        BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct = null;
        try {
            blockChainInfoStruct = blockChainInfoBD.getBlockChainInfo();
            if (blockChainInfoStruct==null){
                Instant time = Instant.now();
            Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                    .setNanos(time.getNano()).build();
            NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.NOT_FOUND).setTimestamp(timestamp).build();
            Status statusErrorResponse = Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("not found blockCHain info")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
            responseObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
            responseObserver.onCompleted();
            }
            GetInfoBlockChainResponse response = GetInfoBlockChainResponse.newBuilder().setHeight(blockChainInfoStruct.height()).setLastHash(blockChainInfoStruct.lastHsh()).build();
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (SQLException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }

    }
    public void startServerAPi()  {
        io.grpc.Server server = null;
        try {
            server = ServerBuilder.forPort(8080).addService(new NodeAPIGrpcServiseImpl(javaChainMethodService)).build();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            System.out.println("err2");
        }
    }

}
