package org.example.CustomBlockChain;


import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.DB.SQL.Wallets.WalletDB;
import org.example.BlockChainBase.Entity.*;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;

import org.example.CustomBlockChain.API.GRPC.NodeAPIGrpcServiseImpl;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.CustomBlockChain.Entity.TypeDownloadRequestNodeCommunication;
import org.example.CustomBlockChain.NodeCommunication.NodeClient;
import org.example.CustomBlockChain.NodeCommunication.NodeServer;
import org.example.CustomBlockChain.Servise.BlockAdderServiseImpl;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;
import org.example.BlockChainBase.Service.ProofOfWorkService;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.Wallet;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class NodeApp {
    final static IpConfigParser ipConfigParser = new IpConfigParser();
    final static String ipAddress;
    final static NodeListDB nodeListDB;
    final static JavaChainMethodService javaChainMethodService;
    final static Asymmetric asymmetricEncoder = new Asymmetric();
    final static NodeServer nodeServer;
    final static NodeClient nodeClient;
    final static Logger logger = Logger.getLogger("NodeApp");
    final static WalletDB walletDB;
    final static HashEncoder hashEncoder = new HashEncoder();
    final static BlockAdderServiseImpl blockAdderServise;
    final static NodeAPIGrpcServiseImpl nodeAPIGrpcServise;
    static Scanner scanner = new Scanner(System.in);

    static {
        try {
            walletDB = new WalletDB();
            ipAddress = ipConfigParser.getIpAddress();
            nodeListDB = new NodeListDB();
            javaChainMethodService = new JavaChainMethodService();
            nodeServer = new NodeServer(javaChainMethodService.getJavaChain());
            nodeClient = new NodeClient(javaChainMethodService.getJavaChain());
            blockAdderServise = new BlockAdderServiseImpl(javaChainMethodService.getJavaChain());
            nodeAPIGrpcServise = new NodeAPIGrpcServiseImpl(javaChainMethodService);
        } catch (IOException | SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void main(String[] args) throws Exception {
        startBlockAdderServise();

        startServersTheard();

        registerNode();


        while (true){
            System.out.println("\nВыберите действие из выпадающего списка:\n1)Синхронизация с блокчейном\n2)Кошелек\n3)Майнинг\n4)Настройки ноды");
            try {
                int numberExecute = scanner.nextInt();
                switch (numberExecute) {
                    //synchronizationBlockChain
                    case 1: {
                        System.out.println("\nЗапуск поиска нод в сети...");
                        if (!synchronizationBlockChain()) {
                            System.out.println("Вы единственная Node в сети");
                        }
                        else {
                            System.out.println("Ваша нода успешно синхонизирована");
                        }
                        break;
                    }
                    case 2: {
                        //wallet
                        System.out.println("1)Мои кошельки\n2)Создать кошелек\n3)Востановить кошелек\n4)Назад");
                        numberExecute = scanner.nextInt();
                        switch (numberExecute) {
                            case 1: {
                                    ArrayList<Wallet> wallets = walletDB.getAllWallets();
                                    if (wallets.isEmpty()){
                                        System.out.println("У вас нет ни 1 кошелька");
                                    }
                                    for (int i = 0;i<wallets.size();i++) {
                                    System.out.printf("%d) %s%n", i + 1, wallets.get(i).getPublicKey());
                                    }
                                    System.out.println("0)exit");

                                    int numberWallet = scanner.nextInt();
                                    while (numberWallet > wallets.size()) {
                                        System.out.println("Вы ввели недействительный номер кошелька");
                                        numberWallet = scanner.nextInt();
                                    }
                                    if (numberWallet == 0) break;
                                    Wallet wallet = wallets.get(numberWallet - 1);
                                    System.out.println("Введите пароль от кошелька");
                                    System.out.println("Для выхода из меню ввода пороля введите: 'exit'");
                                    scanner = new Scanner(System.in);
                                    String password = scanner.nextLine();
                                    while (!checkPasswordInvalid(wallet.getPassword(), password) && !password.equals("exit")) {
                                        System.out.println("Введен неверный пароль!Для выхода из меню ввода пороля введите: 'exit'");
                                        password = scanner.nextLine();
                                    }
                                    if (password.equals("exit")) break;
                                    System.out.println("\nYour Wallet:");
                                    System.out.println("Address: " + wallet.getPublicKey());
                                    Address address = javaChainMethodService.getAddress(wallet.getPublicKey());
                                    int balance;
                                    if (address != null) {
                                        System.out.println("Balance: " + address.getBalance() + " ETH");
                                        balance = address.getBalance();
                                    } else {
                                        System.out.println("Balance: 0 ETH");
                                        balance = 0;
                                    }
                                    System.out.println("\nЧто хотите сделать?\n1)Отправить перевод\n2)Удалить кошелек\n3)Получить Private Key\n0)Exit");
                                    //sendTransfer
                                    numberExecute = scanner.nextInt();
                                    switch (numberExecute) {
                                        case 1: {
                                            System.out.println("Введите аддресс на который хотите отправить: ");
                                            scanner = new Scanner(System.in);
                                            String to = scanner.nextLine();
                                            if (!asymmetricEncoder.isValidPublicKey(to)) {
                                                System.out.println("Некорректный адресс");
                                                break;
                                            }
                                            if (!asymmetricEncoder.isValidPublicKey(wallet.getPublicKey())) break;
                                            System.out.println("Введите суммы, которую хотите отправить");
                                            int value = scanner.nextInt();
                                            if (value < 0 || value > balance || balance==0) {
                                                System.out.println("Некорректная сумма или недостаточный баланс");
                                                break;
                                            }
                                            String hashTransaction = sendTransaction(wallet.getPublicKey(),to,value,wallet.getPrivateKey());
                                            if (hashTransaction==null){
                                                System.out.println("invalid transaction");
                                                break;
                                            }
                                            System.out.println("Успешно отправленно!");
                                            System.out.println("ждите подтверждение отправки транзакции в блокчейне");
                                            System.out.format("Хэш транзакции: %s",hashTransaction);
                                            break;
                                        }
                                        //deleteWallet
                                        case 2: {
                                            System.out.println("Введите пароль от кошелька,который хотите удалить");
                                            scanner = new Scanner(System.in);
                                            password = scanner.nextLine();
                                            if (!checkPasswordInvalid(wallet.getPassword(), password)) {
                                                System.out.println("Неверный пароль");
                                                break;
                                            }
                                            walletDB.deleteByAddress(wallet.getPublicKey());
                                            System.out.println("Кошелек успешно удален!");
                                            break;
                                        }
                                        case 3: {
                                            System.out.println("Введите пароль: ");
                                            scanner = new Scanner(System.in);
                                            password = scanner.nextLine();
                                            if (!checkPasswordInvalid(wallet.getPassword(), password)) {
                                                System.out.println("Неверный пароль");
                                                break;
                                            }
                                            System.out.println("Private Key: " + wallet.getPrivateKey());
                                        }
                                        case 0: {
                                            break;
                                        }
                                    }
                                break;
                            }
                            //createWallet
                            case 2: {
                                System.out.println("Введите пароль от нового кошелька.Длинна пароля от 5 символов");
                                System.out.println("Или введите exit для выхожа из меню.");
                                scanner = new Scanner(System.in);
                                String password = scanner.nextLine();
                                while (!password.equals("exit") && password.length() < 5) {
                                    System.out.println("Пароль слишком короткий,введите пароль длинной более 5 символов");
                                    password = scanner.nextLine();
                                }
                                if (password.equals("exit")) break;
                                if (createNewWallet(password)) System.out.println("Успешно создано");
                                break;
                            }
                            case 3: {
                                //restoreWallet
                                System.out.println("Что хотите сделать?\n1)Востановить при помощи Private key\n0)Exit");
                                numberExecute = scanner.nextInt();
                                switch (numberExecute) {
                                    case 0:
                                        break;
                                    case 1: {
                                        scanner = new Scanner(System.in);
                                        System.out.println("Введите Private Key");
                                        String privateKey = scanner.nextLine();
                                        String password = scanner.nextLine();
                                        System.out.println("Придумайте новый пароль для своего кошелька,минимум 5 символов");
                                        System.out.println("Или введите exit для выхожа из меню.");
                                        while (!password.equals("exit") && password.length() < 5) {
                                            System.out.println("Пароль слишком короткий,введите пароль длинной более 5 символов");
                                            password = scanner.nextLine();
                                        }
                                        if (password.equals("exit")) return;
                                        String result = restoreWallet(privateKey,password);
                                        if (result==null)break;
                                        System.out.println(result);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                    //mining
                    case 3: {
                        System.out.println("Майнинг запущен,для выхода введите 'exit'");
                        Thread miningThread = miningStart();
                        scanner =  new Scanner(System.in);
                        String execute = scanner.nextLine();
                        while (!execute.equals("exit")) execute = scanner.nextLine();
                        miningThread.interrupt();
                        break;
                    }
                    case 4: {
                        System.out.println("Выберите параметр который хотите изменить:\n1)Изменить ip\n2)Аддресс кошелька(для майниннга)");
                        numberExecute = scanner.nextInt();
                        if (numberExecute == 1) {
                            if (ipConfigParser.isHaveProblemIp()) {
                                System.out.println("Введите новый ipv4");
                                String newIp = scanner.nextLine();
                                if (ipConfigParser.editMyIp(newIp)) System.out.println("\nУспешно\n");
                                else System.out.println("\nНе успешно,введен некоректный ip\n");
                            } else System.out.println("\nУ вас нет проблем с ip\n");
                        } else if (numberExecute == 2) {
                            System.out.println("\n1)Ввбрать кошелек из cписка моих кошельков\n2)Ввести кошелек вручную");
                            numberExecute = scanner.nextInt();
                            if (numberExecute == 1) {
                                if (!walletDB.getAllWallets().isEmpty()) {
                                    System.out.println("Your Wallets");
                                    ArrayList<Wallet> wallets = walletDB.getAllWallets();
                                    for (int i = 0; i < wallets.size(); i++) {
                                        System.out.printf("%d) %s%n", i + 1, wallets.get(i).getPublicKey());
                                    }
                                    System.out.println("0) Exit");
                                    int numberWallet = scanner.nextInt();
                                    if (numberWallet == 0) break;
                                    else {
                                        if (numberWallet > wallets.size() || numberWallet < 1) {
                                            System.out.println("Неверный номер кошелька");
                                            break;
                                        }
                                        walletDB.setAddressForMining(wallets.get(numberWallet-1).getPublicKey());
                                        System.out.println("Успешно");

                                    }
                                } else System.out.println("\nУ вас нет ни одного кошелька");
                            } else if (numberExecute == 2) {
                                System.out.println("Введите аддрес на ,который хотите получать награду за майнинг");
                                scanner = new Scanner(System.in);
                                String address = scanner.nextLine();
                                if (!asymmetricEncoder.isValidPublicKey(address)) {
                                    System.out.println("Некорректный аддресс кошелька");
                                }
                                walletDB.setAddressForMining(address);
                                System.out.println("Успешно");

                            } else {
                                System.out.println("Некорректный номер параметра");
                            }

                            break;
                        }
                    }
                }
            }
            catch (InputMismatchException e){
                System.out.println("Введите действительный номер комманды!\n");
            } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());
            }
        }
    }

    private static Thread miningStart() {
        Thread miningThread = new Thread(() -> {
            try {
                String feeRecipient = walletDB.getMyAddressForMining();
                ProofOfWorkService<ArrayList<Transaction>> proofOfWorkService = new ProofOfWorkService<>(javaChainMethodService.getJavaChain(),feeRecipient);

                ArrayDeque<Block<ArrayList<Transaction>>> blockPool;
                while (true) {
                    blockPool= javaChainMethodService.getBlocksPoll();

                    if (!blockPool.isEmpty()) {
                        Block<ArrayList<Transaction>> block = javaChainMethodService.popBlockFromPoll();
                        proofOfWorkService.startMining(javaChainMethodService.getBlockNumber(), javaChainMethodService.updateBlockInformation(block));
                    }
                }
            } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());
            }
        });
        miningThread.start();
        return miningThread;
    }

    private static void registerNode() {
        try {
            if (!nodeListDB.isCreated(ipAddress)) nodeListDB.addNode(ipAddress);
            else nodeListDB.editStatusActive(ipAddress, false);
        }
        catch (Exception err) {
            logger.log(Level.WARNING,err.toString());
        }
    }

    private static void startBlockAdderServise() {
        Thread adderBlockThread =new Thread(()->{
            try {
                blockAdderServise.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        adderBlockThread.start();
    }

    private static void startServersTheard() {
        Thread serverApiThread = new Thread(() -> {
            try {
                nodeAPIGrpcServise.startServerAPi();
            } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());
            }
        });
        serverApiThread.start();

        Thread serverThread = new Thread(() -> {
            try {
                nodeServer.startServerNodeCommunication();
            } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());
            }
        });
        serverThread.start();

    }


    private static String restoreWallet(String privateKey,String password){
        try {
            String publicKey = asymmetricEncoder.getPublicFromPrivateKey(privateKey);
            if (publicKey == null) {
                return "private key invalid";
            }
            if (walletDB.getWalletByAddress(publicKey) != null) {
                return "Данный кошелек уже добавлен!";
            }
            Wallet newWallet = new Wallet(password, publicKey, privateKey);
            walletDB.createNewWallet(newWallet);
            return "Кошелек успешно добавлен!";
        }
        catch (Exception err){
            logger.log(Level.WARNING,"Проищошла ошибка: "+err);
            return null;
        }
    }


    public static boolean synchronizationBlockChain() throws Exception {
        String randomIpNode = nodeListDB.getRandomIp();
        TypeDownloadRequestNodeCommunication typeRequest = TypeDownloadRequestNodeCommunication.ALL;
        while (randomIpNode != null) {
            typeRequest = nodeClient.SynchronizationBlockChain(randomIpNode,javaChainMethodService.getBlockNumber(),typeRequest);
            if (typeRequest==null) break;
            randomIpNode = nodeListDB.getRandomIp();
        }
        if (randomIpNode == null) {
            nodeListDB.editStatusActive(ipAddress, true);
            return false;
        }
        return true;

    }
    public static String sendTransaction(String from,String to,int value,String privateKey){
        try {
            Transaction transaction = Transaction.newTransactionBuilder()
                    .setFrom(from)
                    .setData("send transaction to "+to)
                    .setTo(to).setValue(value)
                    .setNonce(javaChainMethodService.getNoncePending(from))
                    .setSing(javaChainMethodService.singTransaction(privateKey,from,to)).build();
            return javaChainMethodService.sendTransaction(transaction);
        } catch (Exception e) {
                logger.log(Level.WARNING,e.toString());
            return null;
        }
    }
    public static boolean checkPasswordInvalid(String password, String enteredPassword){
        return !hashEncoder.SHA256(password).equals(enteredPassword);
    }
    private static boolean createNewWallet(String password) {
        try {
            Asymmetric.Keys keys = asymmetricEncoder.generateKeys();
            Wallet newWallet = new Wallet(password, keys.publicKey(), keys.privateKey());
            walletDB.createNewWallet(newWallet);
            return true;
        } catch (Exception e) {
            logger.log(Level.WARNING,"Произошла ошибка создание кошелька: "+e);
            return false;
        }
    }

}



