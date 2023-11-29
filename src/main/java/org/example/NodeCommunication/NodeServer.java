package org.example.NodeCommunication;

import com.google.gson.Gson;
import org.example.BlockChain.BlockChainBase;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.DB.SQL.NodeListDB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class NodeServer<T> {
    LevelDbBlock<T> levelDbBlock = new LevelDbBlock();
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
                if (hashLastBlock==null) {
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
