package org.example.NodeCommunication.JavaChainNode;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChain.BlockChainBase;
import org.example.Entity.Transaction;
import org.example.JavaChain;
import org.example.NodeCommunication.NodeClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeJavaChainClient extends NodeClient<ArrayList<Transaction>> {

    Gson gson = new Gson();
    private final JavaChain javaChain;

    public NodeJavaChainClient(JavaChain javaChain) throws SQLException, IOException {
        super(javaChain);
        this.javaChain=javaChain;

    }
    @Override
    public boolean SynchronizationBlockChain(String ip) throws Exception {
        if (!super.SynchronizationBlockChain(ip))return false;
        Socket socket = new Socket(ip, 1238);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out.println("getPoolTransactions");
        out.flush();
        String pollTransactionsJson = in.readLine();
        Type typeJson = new TypeToken<ArrayList<Transaction>>(){}.getType();
        ArrayList<Transaction> poolTransactions = gson.fromJson(pollTransactionsJson,typeJson);
        javaChain.setPoolTransactions(poolTransactions);
        return true;
    }


}
