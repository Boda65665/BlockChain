package org.example.BlockChainApps;

import org.example.BlockChain.BlockChain;
import org.example.Cryptography.HashEncoder;
import org.example.DB.SQL.NodeListDB;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.JavaChain;
import org.example.NodeCommunication.IpConfigParser;
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
        Scanner scanner = new Scanner(System.in);
        IpConfigParser ipConfigParser = new IpConfigParser();
        String ipAddress = ipConfigParser.getIpAddress();
        System.out.println(ipAddress);
        NodeClient<ArrayList<Transaction>> nodeClient = new NodeClient<>(javaChain);

        NodeServer<ArrayList<Transaction>> nodeServer = new NodeServer<>(blockChain);

        NodeListDB nodeListDB = new NodeListDB();
        if (!nodeListDB.isCreated(ipAddress)) nodeListDB.addNode(ipAddress);
        else nodeListDB.editStatusActive(ipAddress,false);
        Thread serverThread = new Thread(() -> {
            try {
                nodeServer.handler();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverThread.start();

        while (true){
            System.out.println("\nВыберите действие из выпадающего списка:\n1)Синхронизация с блокчейном\n2)Кошелек\n3)Майнинг\n4)Настройки ноды");



            try {
                int numberExecute = scanner.nextInt();



                switch (numberExecute){
                case 1:{
                    System.out.println("\nЗапуск поиска нод в сети...");
                    String randomIpNode = nodeListDB.getRandomIp();

                    while (randomIpNode!=null && !nodeClient.SynchronizationBlockChain(randomIpNode)){
                        randomIpNode=nodeListDB.getRandomIp();
                    }
                    if (randomIpNode==null){
                        System.out.println("Вы первая нода в сети!");
                        nodeListDB.editStatusActive(ipAddress, true);
                    }
                    break;
                }
                case 2:
                case 3:{

                }
                case 4:{
                    System.out.println("Выберите параметр который хотите изменить:\n1)Изменить ip\n2)Аддресс кошелька(для майниннга)");
                    numberExecute = scanner.nextInt();
                    if (numberExecute==1) {
                        if (ipConfigParser.isHaveProblemIp()){
                        System.out.println("Введите новый ipv4");
                        String newIp = scanner.nextLine();
                        if (ipConfigParser.editMyIp(newIp)) System.out.println("\nУспешно\n");
                        else System.out.println("\nНе успешно,введен некоректный ip\n");}
                        else System.out.println("\nУ вас нет проблем с ip\n");
                    }
                    else if (numberExecute==2){
                        System.out.println("\nВведите аддресс на которой хотите получать деньги за майнинг или выберит еиз выпадающего списка своих кошелеков");


                    }
                    else System.out.println("\nНевено номер параметра");
                }
                break;


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
