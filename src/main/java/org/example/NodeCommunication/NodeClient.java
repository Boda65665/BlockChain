package org.example.NodeCommunication;

import com.google.gson.reflect.TypeToken;
import org.example.BlockChain.BlockChainBase;
import org.example.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.DB.SQL.Node.NodeListDB;
import org.example.Entity.Block;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.Entity.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeClient<T> {
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    LevelDbBlock<Block<T>> levelDbBlock = new LevelDbBlock<>(typeData);
    private final LevelDbPoolBlock<T> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    LevelDbState levelDbState = new LevelDbState();
    private final BlockChainBase<T> blockChain;
    private final NodeListDB nodeListDB = new NodeListDB();

    public NodeClient(BlockChainBase<T> blockChain) throws SQLException, IOException {
        this.blockChain = blockChain;
    }




    public boolean SynchronizationBlockChain(String IP) throws Exception {
        if (!ping(IP)) return false;
        IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();

        Socket socket = new Socket(IP,1238);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("connect to "+IP+":"+1238);
        System.out.println("Starting download blockChain");
        out.println("full");
        if (!blockChain.getBlocks().isEmpty()) {
            out.println(blockChain.getBlocks().get(blockChain.getBlocks().size() - 1));
        }
        else {
            out.println("");
        }
        if (!blockChain.getAllAddresses().isEmpty()){
            out.println(blockChain.getAllAddresses().get(blockChain.getAllAddresses().size()-1));
        }
        else {
            out.println("");
        }
        out.flush();
        String blocksJson = in.readLine();

        String poolBlocksJson = in.readLine();System.out.println(233);
        String statesJson = in.readLine();

        levelDbBlock.buildBlockChain(blocksJson);

        levelDbPoolBlock.buildBlockChain(poolBlocksJson);

        levelDbState.buildStates(statesJson);
        out.close();
        in.close();
        socket.close();
        System.out.println("finishing download blockchain");

        nodeListDB.editStatusActive(ipAddress,true);
        return true;
    }
    public  boolean ping(String IP) throws Exception {
        Socket socket = null;
        try {
            socket = new Socket(IP,1239);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("connect to "+IP+":"+1239);
            out.println("ping");
            out.flush();

            out.close();
            in.close();
            socket.close();
            return true;
        } catch (IOException e) {
            nodeListDB.editStatusActive(IP,false);
            return false;
        }

    }
    public void update() throws Exception {
        ArrayList<String> IPs = nodeListDB.getAllIp();
        for (String ip : IPs) {
            if (ping(ip)){
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

    }
}
