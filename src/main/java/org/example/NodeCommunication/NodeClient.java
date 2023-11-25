package org.example.NodeCommunication;

import org.example.BlockChain.BlockChainBase;
import org.example.DB.SQL.NodeListDB;
import org.example.Entity.Block;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Scanner;

public class NodeClient<T> {
    LevelDbBlock<Block<T>> levelDbBlock = new LevelDbBlock<>();
    LevelDbState levelDbState = new LevelDbState();
    private final BlockChainBase<T> blockChain;
    private final NodeListDB nodeListDB = new NodeListDB();

    public NodeClient(BlockChainBase<T> blockChain) throws SQLException, IOException {
        this.blockChain = blockChain;
    }

    public void SynchronizationBlockChain(String IP) throws Exception {
        Socket socket = new Socket(IP,1234);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("connect to "+IP+":"+1234);
        System.out.println("Starting download blockChain");
        out.println("full");
        if (!blockChain.getBlocks().isEmpty()) {
            out.println(blockChain.getBlocks().get(blockChain.getBlocks().size() - 1));
            out.println(blockChain.getAllAddresses().get(blockChain.getAllAddresses().size()-1));
        }
        out.flush();
        String blocksJson = in.readLine();
        String poolBlocksJson = in.readLine();
        String statesJson = in.readLine();
        levelDbBlock.buildBlockChain(blocksJson,poolBlocksJson);
        levelDbState.buildStates(statesJson);
        out.close();
        in.close();
        socket.close();
        System.out.println("finishing download blockchain");
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        nodeListDB.editStatusActive(ipAddress,true);
    }
}
