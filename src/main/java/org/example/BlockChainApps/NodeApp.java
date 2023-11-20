package org.example.BlockChainApps;

import org.example.BlockChain.BlockChain;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.JavaChain;
import org.example.NodeCommunication.NodeClient;
import org.example.NodeCommunication.NodeServer;

import java.io.IOException;
import java.net.InetAddress;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class NodeApp {




    public static void main(String[] args) throws IOException, BlockChainException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(new HashEncoder());
        JavaChain javaChain = new JavaChain(blockChain);
        NodeClient<ArrayList<Transaction>> nodeClient = new NodeClient<>(javaChain);
        NodeServer<ArrayList<Transaction>> nodeServer = new NodeServer<>();
        Thread serverThread = new Thread(() -> {
            try {
                nodeServer.handler();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        InetAddress ip = InetAddress.getLocalHost();
        String ipAddress = ip.getHostAddress();
        nodeClient.SynchronizationBlockChain(ipAddress);




    }
}
