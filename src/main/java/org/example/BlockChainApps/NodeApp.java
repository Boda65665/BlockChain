package org.example.BlockChainApps;

import org.example.BlockChain.BlockChain;
import org.example.Cryptography.HashEncoder;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.DB.SQL.Node.NodeListDB;
import org.example.DB.SQL.Wallets.WalletDB;
import org.example.Entity.Transaction;
import org.example.Entity.Wallet;
import org.example.JavaChain;
import org.example.NodeCommunication.IpConfigParser;
import org.example.NodeCommunication.JavaChainNode.NodeJavaChainClient;
import org.example.NodeCommunication.JavaChainNode.NodeJavaChainServer;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class NodeApp {
    public static void main(String[] args) throws Exception {
        WalletDB walletDB = new WalletDB();
        BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(new HashEncoder());
        JavaChain javaChain = new JavaChain(blockChain);
        Scanner scanner = new Scanner(System.in);
        IpConfigParser ipConfigParser = new IpConfigParser();
        String ipAddress = ipConfigParser.getIpAddress();
        System.out.println(ipAddress);
        HashEncoder hashEncoder = new HashEncoder();
        NodeJavaChainClient nodeClient = new NodeJavaChainClient(javaChain);
        LevelDbState levelDbState = new LevelDbState();
        NodeJavaChainServer nodeServer = new NodeJavaChainServer(javaChain);

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
                {
                    System.out.println("1)Мои кошельки\n2)Создать кошелек\n3)Востановить кошелек\n4)Назад");
                    numberExecute = scanner.nextInt();
                    switch (numberExecute){
                        case 1:{
                            ArrayList<Wallet> wallets = walletDB.getAllWallets();
                            if (wallets.isEmpty()){
                                System.out.println("У вас нет еще ни 1 кошелька");
                                break;
                            }
                            else {
                                System.out.println("Выберите кошелек,которым хотите воспользоваться:");
                                for (int i = 0;i<wallets.size();i++) {
                                    System.out.printf("%d) %s%n",i+1,wallets.get(i).getPublicKey());
                                }
                                int numberWallet = scanner.nextInt();
                                while (numberWallet>wallets.size()){
                                    System.out.println("Вы ввели недействительный номер кошелька");
                                    numberWallet=scanner.nextInt();
                                }
                                Wallet wallet = wallets.get(numberWallet);

                                System.out.println("Введите пароль от кошелька");
                                String password = scanner.nextLine();
                                int attempts = 3;
                                while (hashEncoder.SHA256(password).equals(wallet.getPassword()) && attempts!=0) {
                                    attempts--;
                                    System.out.println("\nYour Wallet:\n");
                                    System.out.println("Address: " + wallet.getPublicKey());
                                    if (levelDbState.get(wallet.getPublicKey()) != null)
                                        System.out.println("Balance: " + levelDbState.get(wallet.getPublicKey()) + " ETH");
                                    else System.out.println("Balance: 0 ETH");
                                    System.out.println("Private Key: "+wallet.getPrivateKey());
                                    System.out.println("Secret Phrase: "+wallet.getSecretPhrase());
                                }


                            }
                        }
                        case 4:{
                            break;
                        }
                    }
                    break;
                }
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
