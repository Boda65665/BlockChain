package org.example.CustomBlockChain.NodeCommunication;

import com.google.gson.reflect.TypeToken;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import node.communication.base.NodeCommunicationGrpc;
import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.BlockChain.BlockChainBase;
import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.Entity.Block;

import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.TypeRequestNodeCommunication;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeClient {
    private final Type typeData = new TypeToken<ArrayList<Transaction>>() {
    }.getType();
    LevelDbBlock<Block<ArrayList<Transaction>>> levelDbBlock = new LevelDbBlock<>(typeData);
    private final LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    LevelDbState levelDbState = new LevelDbState();
    private final JavaChain blockChain;
    AESEncryption encryption = new AESEncryption();

    private final ConverterServiseGrpcEntityCustom converterServiseGrpc = new ConverterServiseGrpcEntityCustom();
    private final NodeListDB nodeListDB = new NodeListDB();

    public NodeClient(JavaChain blockChain) throws SQLException, IOException, ClassNotFoundException {
        this.blockChain = blockChain;
    }


    public TypeRequestNodeCommunication SynchronizationBlockChain(String IP, int numberBlock,TypeRequestNodeCommunication typeRequest) throws Exception {
        if (!ping(IP)) {
            nodeListDB.editStatusActive(IP,false);
            return TypeRequestNodeCommunication.ALL;
        }
        IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
        NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
        if (typeRequest==TypeRequestNodeCommunication.ALL) {
            NodeCommunicationServer.DownloadRequest downloadRequest = NodeCommunicationServer.DownloadRequest.newBuilder().setLastNumberBlock(numberBlock).setType("ALL").build();
            NodeCommunicationServer.DownloadResponse response = stub.download(downloadRequest);

            BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct = new BlockChainInfoBD.BlockChainInfoStruct(encryption.decode(response.getBlockChainInfo().getHashLastBlock()), Integer.parseInt(encryption.decode(response.getBlockChainInfo().getNumberLastBlock())));
            if (!blockChain.isQueryValid(response.getBlocksList().getLast().getHash(), response.getBlocksList().getLast().getBlockNumber(), blockChainInfoStruct)) {
                nodeListDB.editStatusActive(IP, false);
                return TypeRequestNodeCommunication.ALL;
            }
            ArrayList<Block<ArrayList<Transaction>>> blocks = new ArrayList<>();
            for (Entity.Block block : response.getBlocksList()) blocks.add(converterServiseGrpc.grpcBlockToBlock(block));
            blockChain.addAll(blocks);

            if (!downloadOnlyPools(response.getPoolTransactionsList(),response.getPoolBLocksList())) {
                nodeListDB.editStatusActive(IP, false);
                return TypeRequestNodeCommunication.ONLY_POOLS;
            }
        }
        else {
            NodeCommunicationServer.DownloadRequest downloadRequest = NodeCommunicationServer.DownloadRequest.newBuilder().setLastNumberBlock(numberBlock).setType("ONLY_POOLS").build();
            NodeCommunicationServer.DownloadResponse response = stub.download(downloadRequest);
            if (!downloadOnlyPools(response.getPoolTransactionsList(),response.getPoolBLocksList())) {
                nodeListDB.editStatusActive(IP, false);
                return TypeRequestNodeCommunication.ONLY_POOLS;
            }
        }
        nodeListDB.editStatusActive(ipAddress,true);
        return null;
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
            if (err.getStatus().getCode() == Status.Code.UNAVAILABLE) {
            } else {
                // Логирование других ошибок
                System.err.println("Error while pinging server: " + err.getStatus());
                // Считаем, что сервер включен
            }
            return false;
        }
    }

    public void update() throws Exception {

    }
    public boolean downloadOnlyPools(List<Entity.Transaction> transactionsGrpc, List<Entity.Block> blocksPoolGrpc) throws Exception {

        if (!blockChain.isValidPools(blocksPoolGrpc, transactionsGrpc)) {
            return false;
        }
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayList<Block<ArrayList<Transaction>>> blocksPool = new ArrayList<>();

        for (Entity.Block block : blocksPoolGrpc) blocksPool.add(converterServiseGrpc.grpcBlockToBlock(block));
        for (Entity.Transaction transaction : transactionsGrpc) transactions.add(converterServiseGrpc.grpcDataToData(transaction));
        blockChain.addAllToBlockPoll(blocksPool);
        blockChain.addAllTransactionToPoolTransactions(transactions);
        return true;
    }


}
