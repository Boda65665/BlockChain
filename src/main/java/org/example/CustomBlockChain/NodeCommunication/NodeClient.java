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
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.Entity.Block;

import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeClient {
    private final Type typeData = new TypeToken<ArrayList<Transaction>>() {
    }.getType();
    LevelDbBlock<Block<ArrayList<Transaction>>> levelDbBlock = new LevelDbBlock<>(typeData);
    private final LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    LevelDbState levelDbState = new LevelDbState();
    private final BlockChainBase<ArrayList<Transaction>> blockChain;
    private final ConverterServiseGrpcEntityCustom converterServiseGrpc = new ConverterServiseGrpcEntityCustom();
    private final NodeListDB nodeListDB = new NodeListDB();

    public NodeClient(BlockChainBase<ArrayList<Transaction>> blockChain) throws SQLException, IOException, ClassNotFoundException {
        this.blockChain = blockChain;
    }


    public boolean SynchronizationBlockChain(String IP,int numberBlock) throws Exception {
        if (!ping(IP)) return false;
        IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();
        System.out.println("connect to "+IP+":"+8081);
        System.out.println("Starting download blockChain");
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8081").usePlaintext().build();
        NodeCommunicationGrpc.NodeCommunicationBlockingStub stub = NodeCommunicationGrpc.newBlockingStub(managedChannel);
        NodeCommunicationServer.DownloadRequest downloadRequest = NodeCommunicationServer.DownloadRequest.newBuilder().setLastNumberBlock(numberBlock).build();
        ArrayList<Entity.Block> blocksGrpc = new ArrayList<>(stub.download(downloadRequest).getBlocksList());
        ArrayList<Block<ArrayList<Transaction>>> blocks = new ArrayList<>();
        for (Entity.Block block : blocksGrpc) {
            blocks.add(converterServiseGrpc.grpcBlockToBlock(block));
        }
        if (!blockChain.isValid(blocks)){
            nodeListDB.editStatusActive(IP,false);
            return false;
        }
        blockChain.addAll(blocks);

        System.out.println();
        System.out.println("finishing download blockchain");

        nodeListDB.editStatusActive(ipAddress,true);
        return true;
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
        ArrayList<String> IPs = nodeListDB.getAllIp();
        for (String ip : IPs) {
                Socket socket = new Socket(ip,1239);
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out.println("updateCall");
                out.flush();
                out.println();
                in.close();
                socket.close();
            }


    }

    public static void main(String[] args) throws Exception {
        NodeClient nodeClient = new NodeClient(new BlockChain<ArrayList<Transaction>>(new HashEncoder()));
        IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();
        System.out.println(nodeClient.SynchronizationBlockChain(ipAddress,1));
    }
}
