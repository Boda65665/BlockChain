package org.example.BlockChainApps;

import org.example.BlockChain.BlockChain;
import org.example.Cryptography.HashEncoder;
import org.example.DB.SQL.NodeListDB;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class NodeApp {
    public static void main(String[] args) throws Exception {
        BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(new HashEncoder());
        JavaChain javaChain = new JavaChain(blockChain);
        InetAddress ip = InetAddress.getLocalHost();
        Scanner scanner = new Scanner(System.in);
        String ipAddress = ip.getHostAddress();
        System.out.println(ipAddress);
        NodeClient<ArrayList<Transaction>> nodeClient = new NodeClient<>(javaChain);
        NodeServer<ArrayList<Transaction>> nodeServer = new NodeServer<>(blockChain);
        NodeListDB nodeListDB = new NodeListDB();
        if (!nodeListDB.isCreated(ipAddress)) nodeListDB.addNode(ipAddress);
        Thread serverThread = new Thread(() -> {
            try {
                nodeServer.handler();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
        while (true){


            System.out.println("Выберите действие из выпадающего списка:\n1)Синхронизация с блокчейном\n2)Кошелек\n3)Майнинг");

            try {
                int numberExecute = scanner.nextInt();



                switch (numberExecute){
                case 1:{
                    String randomIpNode = nodeListDB.getRandomIp();

                    if (randomIpNode!=null) {
                        nodeClient.SynchronizationBlockChain(ipAddress);
                    }
                    else {
                        nodeListDB.editStatusActive(ipAddress, true);
                    }
                }
                case 2:
                case 3:
                break;
                default:
                    System.out.println("Введите действительный номер комманды!\n");;

            }
            }
            catch (InputMismatchException e){
                System.out.println("Введите действительный номер комманды!\n");;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }





    }
}
