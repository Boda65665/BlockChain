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

import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.TypeRequestNodeCommunication;
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
    private final BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    AESEncryption encryption = new AESEncryption();
    NodeClient nodeClient;
    private final ConverterServiseGrpcEntityCustom converterServise = new ConverterServiseGrpcEntityCustom();
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
        TypeRequestNodeCommunication typeRequest = TypeRequestNodeCommunication.valueOf(request.getType());
        NodeCommunicationServer.DownloadResponse downloadResponse;
        if (typeRequest==TypeRequestNodeCommunication.ALL) {
            BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct = blockChainInfoBD.getBlockChainInfo();
            ArrayList<Entity.Block> blocksGrpc = converterServise.convertAllBlock(blockChain.getBlocksStartingFrom(request.getLastNumberBlock()));
            Entity.BlockChainInfoConstruct blockChainInfoConstruct = Entity.BlockChainInfoConstruct.newBuilder()
                    .setHashLastBlock(encryption.encode(blockChainInfoStruct.lastHsh()))
                    .setNumberLastBlock(encryption.encode(String.valueOf(blockChainInfoStruct.height())))
                    .build();
            downloadResponse = downloadOnlyPools().setBlockChainInfo(blockChainInfoConstruct).addAllBlocks(blocksGrpc).build();
        }
        else {
           downloadResponse = downloadOnlyPools().build();
        }
        responseObserver.onNext(downloadResponse);
        responseObserver.onCompleted();
    }
        catch (Exception er){
            er.printStackTrace();
        }
    }

    private NodeCommunicationServer.DownloadResponse.Builder downloadOnlyPools() throws IOException {
        ArrayList<Entity.Transaction> transactionsGrpc = converterServise.convertAllData(blockChain.getPoolTransactions());
        ArrayList<Entity.Block> blocksPoolGrpc = converterServise.convertAllBlock(new ArrayList<>(blockChain.getBlocksPool()));
        return NodeCommunicationServer.DownloadResponse.newBuilder().addAllPoolBLocks(blocksPoolGrpc).addAllPoolTransactions(transactionsGrpc);
    }

    @Override
    public void ping(NodeCommunicationServer.PingRequest request, StreamObserver<NodeCommunicationServer.PingResponse> responseObserver) {
        NodeCommunicationServer.PingResponse pingResponse = NodeCommunicationServer.PingResponse.newBuilder().build();
        responseObserver.onNext(pingResponse);
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