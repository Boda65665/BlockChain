package org.example.CustomBlockChain.NodeCommunication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.rpc.Status;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import node.communication.base.NodeCommunicationGrpc;
import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;
import org.example.BlockChainBase.BlockChain.BlockChainBase;

import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.CustomBlockChain.Servise.ValidNodeCommunicationServise;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.time.chrono.JapaneseChronology;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class NodeServer extends NodeCommunicationGrpc.NodeCommunicationImplBase{
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();

    LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);

    LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    LevelDbState levelDbState = new LevelDbState();
    Gson gson = new Gson();
    NodeClient nodeClient;
    private final ConverterServiseGrpcEntityCustom converterServise = new ConverterServiseGrpcEntityCustom();
    NodeListDB nodeListDB = new NodeListDB();
    private final JavaChain blockChain;

    private final ValidNodeCommunicationServise validNodeCommunicationServise;


    public NodeServer(JavaChain blockChain) throws SQLException, IOException, ClassNotFoundException {
        this.blockChain = blockChain;
        this.nodeClient=new NodeClient(blockChain);
        this.validNodeCommunicationServise = new ValidNodeCommunicationServise(blockChain);
        }

    @Override
    public void download(NodeCommunicationServer.DownloadRequest request,io.grpc.stub.StreamObserver<NodeCommunicationServer.DownloadResponse> responseObserver)  {
        try {
        Status statusErrorResponse = validNodeCommunicationServise.validDownloadRequest(request);
        if (statusErrorResponse != null) {
            responseObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
            responseObserver.onCompleted();
        }
        ArrayList<Block<ArrayList<Transaction>>> blocks = blockChain.getBlocksStartingFrom(request.getLastNumberBlock());
        ArrayDeque<Block<ArrayList<Transaction>>> blocksPool = blockChain.getBlocksPool();
        ArrayList<Transaction> transactions = blockChain.getPoolTransactions();
        ArrayList<Entity.Transaction> transactionsGrpc = new ArrayList<>();
        ArrayList<Entity.Block> blocksGrpc = new ArrayList<>();
        ArrayList<Entity.Block> blocksPoolGrpc = new ArrayList<>();
        for (Transaction transaction : transactions) {
            transactionsGrpc.add(converterServise.dataBlockToGrpcData(transaction));
        }
        for (Block<ArrayList<Transaction>> block : blocksPool) {
            blocksPoolGrpc.add(converterServise.blockToGrpcBlock(block));
        }
        for (Block<ArrayList<Transaction>> block : blocks) {
            blocksGrpc.add(converterServise.blockToGrpcBlock(block));
        }
        NodeCommunicationServer.DownloadResponse downloadResponse = NodeCommunicationServer.DownloadResponse.newBuilder().addAllPoolTransactions(transactionsGrpc).addAllPoolBLocks(blocksPoolGrpc).addAllBlocks(blocksGrpc).build();
        responseObserver.onNext(downloadResponse);
        responseObserver.onCompleted();
    }
        catch (Exception er){
            er.printStackTrace();
        }
    }
    @Override
    public void ping(NodeCommunicationServer.PingRequest request, StreamObserver<NodeCommunicationServer.PingResponse> responseObserver) {
        NodeCommunicationServer.PingResponse pingResponse = NodeCommunicationServer.PingResponse.newBuilder().build();
        responseObserver.onNext(pingResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void test(node.communication.base.NodeCommunicationServer.TestRequest request,
                     io.grpc.stub.StreamObserver<node.communication.base.NodeCommunicationServer.TestResponse> responseObserver) {
        responseObserver.onNext(NodeCommunicationServer.TestResponse.newBuilder().setTest("1").build());
        responseObserver.onCompleted();
    }
    public void startServerNodeCommunication() throws SQLException, IOException, ClassNotFoundException {
        io.grpc.Server server = ServerBuilder.forPort(8081).addService(new NodeServer(blockChain)).build();
        try {

            server.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}