package org.example.NodeCommunication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChain.BlockChainBase;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.DB.SQL.Node.NodeListDB;
import org.example.Entity.Transaction;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeServer<T> {
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();

    LevelDbBlock<T> levelDbBlock = new LevelDbBlock<>(typeData);

    LevelDbPoolBlock<T> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    LevelDbState levelDbState = new LevelDbState();
    Gson gson = new Gson();
    NodeClient<T> nodeClient;
    NodeListDB nodeListDB = new NodeListDB();
    private final BlockChainBase<T> blockChain;

    public NodeServer(BlockChainBase<T> blockChain) throws SQLException, IOException {
        this.blockChain = blockChain;
        this.nodeClient=new NodeClient<>(blockChain);
    }

    public void handler() throws Exception {
        ServerSocket serverSocket = new ServerSocket(1238);
        while (true) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("connecting "+clientSocket.getInetAddress().getHostAddress());
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            if (request.equals("full")) {
                String hashLastBlock = in.readLine();
                String lastAddress = in.readLine();
                if (hashLastBlock.isEmpty()) {

                    out.println(gson.toJson(blockChain.getBlocks()));
                    out.println(gson.toJson(blockChain.getBlocksPool()));
                    out.println(gson.toJson(blockChain.getAllAddresses()));
                }
                else {
                    if (levelDbBlock.get(hashLastBlock)!=null){
                            out.println(gson.toJson(blockChain.getBlocksStartingFrom(hashLastBlock)));
                            out.println(gson.toJson(blockChain.getBlocksPool()));
                            out.println(gson.toJson(blockChain.getAddressStartingFrom(lastAddress)));
                    }
                    else {
                        out.println(gson.toJson(blockChain.getBlocks()));
                        out.println(gson.toJson(blockChain.getBlocksPool()));
                        out.println(gson.toJson(blockChain.getAllAddresses()));
                    }

                }

            }
            else if(request.equals("ping")) {
                out.println("pong");
            } else if (request.equals("updateCall")) {
                nodeClient.SynchronizationBlockChain(nodeListDB.getRandomIp());
            } else {
                out.println("Invalid request");
            }
            out.flush();
            in.close();
            out.close();
            clientSocket.close();

        }

    }
}
