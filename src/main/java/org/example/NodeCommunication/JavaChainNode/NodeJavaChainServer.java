//package org.example.NodeCommunication.JavaChainNode;
//
//import com.google.gson.Gson;
//import org.example.BlockChain.BlockChainBase;
//import org.example.Entity.Transaction;
//import org.example.JavaChain;
//import org.example.NodeCommunication.NodeServer;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//public class NodeJavaChainServer extends NodeServer<ArrayList<Transaction>> {
//    Gson gson = new Gson();
//    private final JavaChain javaChain;
//
//    public NodeJavaChainServer(JavaChain javaChain) throws SQLException, IOException {
//        super(javaChain);
//        this.javaChain = javaChain;
//    }
//
//    @Override
//    public void handler() throws IOException {
//        Thread threadHandlerBase = new Thread(() -> {
//            try {
//                super.handler();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        });
//        threadHandlerBase.start();
//        ServerSocket serverSocket = new ServerSocket(1240);
//        while (true) {
//            Socket clientSocket = serverSocket.accept();
//            System.out.println("connecting " + clientSocket.getInetAddress().getHostAddress());
//            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//
//            String request = in.readLine();
//            if (request.equals("getPoolTransactions")) {
//                out.println(gson.toJson(javaChain.getPoolTransactions()));
//            }
//        }
//    }
//}
