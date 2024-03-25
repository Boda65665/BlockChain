package org.example.CustomBlockChain.NodeCommunication;

import com.google.rpc.Status;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.StatusProto;
import io.grpc.stub.StreamObserver;
import node.communication.base.NodeCommunicationGrpc;
import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;

import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.DB.LevelDB.TransactonPool.LevelDbTransactionPool;
import org.example.CustomBlockChain.Entity.TypeDownloadRequestNodeCommunication;
import org.example.CustomBlockChain.Entity.TypeUpdateRequestNodeCommunication;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;
import org.example.CustomBlockChain.Servise.Validation.ValidNodeCommunicationServise;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

public class NodeServer extends NodeCommunicationGrpc.NodeCommunicationImplBase{
    private final BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    AESEncryption encryption = new AESEncryption();
    NodeClient nodeClient;
    private final ConverterServiseGrpcEntityCustom converterServise = new ConverterServiseGrpcEntityCustom();
    private final JavaChain blockChain;
    private final LevelDbTransactionPool levelDbTransactionPool = new LevelDbTransactionPool();

    private final ValidNodeCommunicationServise validNodeCommunicationServise;
    private final Logger logger = Logger.getLogger("NodeServer");


    public NodeServer(JavaChain blockChain) throws SQLException, IOException, ClassNotFoundException {
        this.blockChain = blockChain;
        this.nodeClient=new NodeClient(blockChain);
        this.validNodeCommunicationServise = new ValidNodeCommunicationServise(blockChain);
        }
        @Override
        public void isUpdate(NodeCommunicationServer.IsUpdateRequest request, StreamObserver<NodeCommunicationServer.IsUpdateResponse> responseObserver) {;
            try {
                NodeCommunicationServer.IsUpdateResponse.Builder isUpdateResponseBuilder = NodeCommunicationServer.IsUpdateResponse.newBuilder();
                Status statusErrorResponse = validNodeCommunicationServise.validIsUpdateStatus(request);
                isUpdateResponseBuilder.setIsUpdate(statusErrorResponse != null);
                responseObserver.onNext(isUpdateResponseBuilder.build());
                responseObserver.onCompleted();
            } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());

            }
        }
        @Override
        public void update(NodeCommunicationServer.UpdateRequest request, StreamObserver<NodeCommunicationServer.UpdateResponse> responseObserver) {;
            NodeCommunicationServer.UpdateResponse.Builder updateResponseBuilder = NodeCommunicationServer.UpdateResponse.newBuilder();
            try {

                Status statusErrorResponse = validNodeCommunicationServise.validUpdateRequest(request);
                if (statusErrorResponse==null) {

                    switch (TypeUpdateRequestNodeCommunication.valueOf(request.getDataCase().getNumber())) {
                        case BLOCK -> blockChain.addBlock(converterServise.grpcBlockToBlock(request.getBlock()));
                        case TRANSACTION_PENDING -> levelDbTransactionPool.put(converterServise.grpcDataToData(request.getTransaction()));
                    }
                }
                updateResponseBuilder.setIsUpdate(statusErrorResponse == null);
                responseObserver.onNext(updateResponseBuilder.build());
                responseObserver.onCompleted();
            }
            catch (Exception err){
                err.printStackTrace();
                responseObserver.onNext(updateResponseBuilder.setIsUpdate(false).build());
                responseObserver.onCompleted();
            }
        }



    @Override
    public void download(NodeCommunicationServer.DownloadRequest request,io.grpc.stub.StreamObserver<NodeCommunicationServer.DownloadResponse> responseObserver)  {
        try {
        Status statusErrorResponse = validNodeCommunicationServise.validDownloadRequest(request);
        if (statusErrorResponse != null) {
            responseObserver.onError(StatusProto.toStatusRuntimeException(statusErrorResponse));
            responseObserver.onCompleted();
        }
        TypeDownloadRequestNodeCommunication typeRequest = TypeDownloadRequestNodeCommunication.valueOf(request.getType());
        NodeCommunicationServer.DownloadResponse downloadResponse;
        if (typeRequest== TypeDownloadRequestNodeCommunication.ALL) {
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
            logger.log(Level.WARNING,er.toString());

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
            logger.log(Level.WARNING,e.toString());

        }
        try {
            server.awaitTermination();
        } catch (InterruptedException e) {
            logger.log(Level.WARNING,e.toString());
        }

    }


}