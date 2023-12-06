package org.example.BlockChainApps;

import net.sf.extjwnl.JWNLException;
import net.sf.extjwnl.data.IndexWord;
import net.sf.extjwnl.data.POS;
import net.sf.extjwnl.dictionary.Dictionary;
import org.example.BlockChain.BlockChain;
import org.example.Cryptography.AESEncryption;
import org.example.Cryptography.Asymmetric;
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
        generateSecretPhrase();
        final WalletDB walletDB = new WalletDB();
        final BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(new HashEncoder());
        final JavaChain javaChain = new JavaChain(blockChain);
        final Scanner scanner = new Scanner(System.in);
        final IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();
        System.out.println(ipAddress);
        final HashEncoder hashEncoder = new HashEncoder();
        final NodeJavaChainClient nodeClient = new NodeJavaChainClient(javaChain);
        final LevelDbState levelDbState = new LevelDbState();
        final NodeJavaChainServer nodeServer = new NodeJavaChainServer(javaChain);
        final AESEncryption encryption = new AESEncryption();
        final NodeListDB nodeListDB = new NodeListDB();
        final Asymmetric asymmetricEncoder = new Asymmetric();
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
                                System.out.println("0) Exit");
                                int numberWallet = scanner.nextInt();
                                while (numberWallet>wallets.size()){
                                    System.out.println("Вы ввели недействительный номер кошелька");
                                    numberWallet=scanner.nextInt();
                                }
                                if (numberWallet==0) break;
                                Wallet wallet = wallets.get(numberWallet-1);

                                System.out.println("Введите пароль от кошелька");
                                System.out.println("Для выхода из меню ввода пороля введите: 'exit'");
                                String password = scanner.nextLine();
                                while (!password.equals("exit") && !hashEncoder.SHA256(password).equals(wallet.getPassword())) {
                                    password = scanner.nextLine();
                                    System.out.println("Введен неверный пароль!Для выхода из меню ввода пороля введите: 'exit'");
                                }
                                if (password.equals("exit"))break;
                                System.out.println("\nYour Wallet:");
                                System.out.println("Address: " + wallet.getPublicKey());
                                if (levelDbState.get(wallet.getPublicKey()) != null) System.out.println("Balance: " + levelDbState.get(wallet.getPublicKey()) + " ETH");
                                else System.out.println("Balance: 0 ETH");
                                System.out.println("Private Key: "+encryption.decode(wallet.getPrivateKey()));
                                System.out.println("Secret Phrase: "+encryption.encode(wallet.getSecretPhrase()));
                            }
                        }
                        case 2:{
                            System.out.println("Введите пароль от нового кошелька.Длинна пароля от 5 символов");
                            System.out.println("Или введите exit для выхожа из меню.");
                            String password = scanner.nextLine();
                            while (!password.equals("exit") && password.length()<5){
                                password=scanner.nextLine();
                                System.out.println("Пароль слишком короткий,введите пароль длинной более 5 символов");
                            }
                            if (password.equals("exit"))break;
                            Asymmetric.Keys keys = asymmetricEncoder.generateKeys();
//                            Wallet newWallet = new Wallet(password,keys.publicKey(),keys.privateKey(),generateSecretPhrase());
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
    private static String generateSecretPhrase() throws JWNLException {
        Dictionary dictionary = Dictionary.getDefaultResourceInstance();
        // Создание списка для хранения случайных слов
        StringBuilder secretePhrase = new StringBuilder();
        // Генерация 12 случайных слов
        while (secretePhrase.toString().split(" ").length<12) {
            // Получение случайного индекса слова
            IndexWord indexWord = dictionary.getRandomIndexWord(POS.NOUN);
            // Получение слова по индексу
            String word = indexWord.getLemma();
            if(word.split(" ").length>1) continue;
            // Добавление слова в список
            secretePhrase.append(word).append(" ");
        }
        // Вывод случайных слов

        return secretePhrase.toString().trim();
    }

}



