package org.example.CustomBlockChain.NodeCommunication;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import node.communication.base.NodeCommunicationGrpc;
import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;
import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.Entity.Block;

import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.TypeDownloadRequestNodeCommunication;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;

import java.io.IOException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NodeClient {
    private final JavaChain blockChain;
    AESEncryption encryption = new AESEncryption();
    Logger logger = Logger.getLogger("NodeClient");
    private final ConverterServiseGrpcEntityCustom converterServiseGrpc = new ConverterServiseGrpcEntityCustom();
    private final NodeListDB nodeListDB = new NodeListDB();
    BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    public NodeClient(JavaChain blockChain) throws SQLException, IOException, ClassNotFoundException {
        this.blockChain = blockChain;
    }


    public TypeDownloadRequestNodeCommunication SynchronizationBlockChain(String IP, int numberBlock, TypeDownloadRequestNodeCommunication typeRequest) {
        try {
            if (!ping(IP)) {
                nodeListDB.editStatusActive(IP, false);
                return TypeDownloadRequestNodeCommunication.ALL;
            }
            IpConfigParser ipConfigParser = new IpConfigParser();
            final String ipAddress = ipConfigParser.getIpAddress();
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
            NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
            if (typeRequest == TypeDownloadRequestNodeCommunication.ALL) {
                NodeCommunicationServer.DownloadRequest downloadRequest = NodeCommunicationServer.DownloadRequest.newBuilder().setLastNumberBlock(numberBlock).setType("ALL").build();
                NodeCommunicationServer.DownloadResponse response = stub.download(downloadRequest);

                BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct = new BlockChainInfoBD.BlockChainInfoStruct(encryption.decode(response.getBlockChainInfo().getHashLastBlock()), Integer.parseInt(encryption.decode(response.getBlockChainInfo().getNumberLastBlock())));
                if (!blockChain.isQueryValid(response.getBlocksList().getLast().getHash(), response.getBlocksList().getLast().getBlockNumber(),blockChainInfoStruct)) {
                    nodeListDB.editStatusActive(IP, false);
                    return TypeDownloadRequestNodeCommunication.ALL;
                }
                ArrayList<Block<ArrayList<Transaction>>> blocks = converterServiseGrpc.convertAllGrpcBlock(response.getBlocksList());
                blockChain.addAll(blocks);
                if (!downloadOnlyPools(response.getPoolTransactionsList(), response.getPoolBLocksList())) {
                    nodeListDB.editStatusActive(IP, false);
                    return TypeDownloadRequestNodeCommunication.ONLY_POOLS;
                }
            } else {
                NodeCommunicationServer.DownloadRequest downloadRequest = NodeCommunicationServer.DownloadRequest.newBuilder().setLastNumberBlock(numberBlock).setType("ONLY_POOLS").build();
                NodeCommunicationServer.DownloadResponse response = stub.download(downloadRequest);
                if (!downloadOnlyPools(response.getPoolTransactionsList(), response.getPoolBLocksList())) {
                    nodeListDB.editStatusActive(IP, false);
                    return TypeDownloadRequestNodeCommunication.ONLY_POOLS;
                }
            }
            nodeListDB.editStatusActive(ipAddress, true);
            return null;
        } catch (Exception err) {
            logger.log(Level.WARNING,err.toString());
            return typeRequest;
        }
    }


    private boolean ping(String ip) {
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
            NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
            NodeCommunicationServer.PingRequest pingRequest = NodeCommunicationServer.PingRequest.newBuilder().build();
            stub.ping(pingRequest);
            return true;
        }
        catch (StatusRuntimeException err){
            logger.log(Level.WARNING,err.toString());
            return false;
        }
    }


    public boolean update(Transaction transaction) {
        return isNotUpdated(NodeCommunicationServer.IsUpdateRequest.newBuilder().setLastTransactionHash(transaction.getHash()).build()) && update(NodeCommunicationServer.UpdateRequest.newBuilder().setTransaction(converterServiseGrpc.dataBlockToGrpcData(transaction)));
    }
    public boolean update(Block<ArrayList<Transaction>> block){
        return isNotUpdated(NodeCommunicationServer.IsUpdateRequest.newBuilder().setLastNumberBlock(block.getBlockNumber()).build()) && update(NodeCommunicationServer.UpdateRequest.newBuilder().setBlock(converterServiseGrpc.blockToGrpcBlock(block)));
    }
    private boolean update(NodeCommunicationServer.UpdateRequest.Builder requestBuilder){
        try {
            requestBuilder.setBlockChainInfo(converterServiseGrpc.blockChainInfoBaseToBlockChainInfoGrpc(blockChainInfoBD.getBlockChainInfo()));
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
            NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
            NodeCommunicationServer.UpdateResponse response = stub.update(requestBuilder.build());
            managedChannel.shutdown();
            return response.getIsUpdate();
        }
        catch (Exception err){
            logger.log(Level.WARNING,err.toString());
            return false;
        }
        }

    private boolean isNotUpdated(NodeCommunicationServer.IsUpdateRequest request){
        try {
            ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
            NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
            NodeCommunicationServer.IsUpdateResponse response = stub.isUpdate(request);
            managedChannel.shutdown();
            return !response.getIsUpdate();
        }
        catch (Exception err){
            logger.log(Level.WARNING,err.toString());
            return true;
        }
    }
    public boolean downloadOnlyPools(List<Entity.Transaction> transactionsGrpc, List<Entity.Block> blocksPoolGrpc) throws Exception {
        if (!blockChain.isValidPools(converterServiseGrpc.convertAllGrpcBlock(blocksPoolGrpc), converterServiseGrpc.convertAllGrpcData(transactionsGrpc))) {
            return false;
        }
        ArrayList<Transaction> transactions = converterServiseGrpc.convertAllGrpcData(transactionsGrpc);
        ArrayList<Block<ArrayList<Transaction>>> blocksPool = converterServiseGrpc.convertAllGrpcBlock(blocksPoolGrpc);
        blockChain.addAllToBlockPoll(blocksPool);
        blockChain.addAllTransactionToPoolTransactions(transactions);
        return true;
    }


}
