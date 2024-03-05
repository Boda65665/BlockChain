package org.example.CustomBlockChain;

import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.DB.SQL.Wallets.WalletDB;
import org.example.BlockChainBase.Entity.*;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;

import org.example.CustomBlockChain.API.GRPC.NodeAPIGrpcServiseImpl;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.CustomBlockChain.DB.LevelDB.NodeCommunication.NodeServer;
import org.example.CustomBlockChain.Servise.BlockAdderServiseImpl;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;
import org.example.BlockChainBase.Service.ProofOfWorkService;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.Wallet;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Hello world!
 *
 */
public class NodeApp {
    public static void main(String[] args) throws Exception {
        final WalletDB walletDB = new WalletDB();
        JavaChainMethodService javaChainMethodService = new JavaChainMethodService();
        BlockAdderServiseImpl blockAdderServise = new BlockAdderServiseImpl(javaChainMethodService.getJavaChain());
        NodeAPIGrpcServiseImpl nodeAPIGrpcServise = new NodeAPIGrpcServiseImpl(javaChainMethodService);
        Thread adderBlockThread =new Thread(()->{
            try {
                blockAdderServise.run();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        adderBlockThread.start();
        Thread serverApiThread = new Thread(() -> {
//            System.setErr(new PrintStream(new OutputStream() {
//                @Override
//                public void write(int b) {
//                    // Игнорируем сообщения об ошибках
//                }
//            }));
            try {
                nodeAPIGrpcServise.startServerAPi();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        serverApiThread.start();
        Scanner scanner = new Scanner(System.in);
        final HashEncoder hashEncoder = new HashEncoder();
        final NodeServer nodeServer = new NodeServer(javaChainMethodService.getBlockChain());
        final IpConfigParser ipConfigParser = new IpConfigParser();
        final String ipAddress = ipConfigParser.getIpAddress();
        final NodeListDB nodeListDB = new NodeListDB();
        final Asymmetric asymmetricEncoder = new Asymmetric();
        if (!nodeListDB.isCreated(ipAddress)) nodeListDB.addNode(ipAddress);
        else nodeListDB.editStatusActive(ipAddress,false);
        //startServerNode
        Thread serverThread = new Thread(() -> {
            try {
                nodeServer.startServerNodeCommunication();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        serverThread.start();
        while (true){
            System.out.println("\nВыберите действие из выпадающего списка:\n1)Синхронизация с блокчейном\n2)Кошелек\n3)Майнинг\n4)Настройки ноды");
            try {
                int numberExecute = scanner.nextInt();
                switch (numberExecute) {
                    //synchronizationBlockChain
                    case 1: {
                        System.out.println("\nЗапуск поиска нод в сети...");
                        if (!javaChainMethodService.synchronizationBlockChain()) {
                            System.out.println("Вы единственная Node в сети");
                        }
                        break;
                    }
                    case 2: {
                        //wallet
                        LevelDbState levelDbState = new LevelDbState();

                        System.out.println("1)Мои кошельки\n2)Создать кошелек\n3)Востановить кошелек\n4)Назад");
                        numberExecute = scanner.nextInt();
                        switch (numberExecute) {
                            //myWallets
                            case 1: {
                                ArrayList<Wallet> wallets = walletDB.getAllWallets();
                                if (wallets.isEmpty()) {
                                    System.out.println("У вас нет еще ни 1 кошелька");
                                    break;
                                } else {
                                    System.out.println("Выберите кошелек,которым хотите воспользоваться:");
                                    for (int i = 0; i < wallets.size(); i++) {
                                        System.out.printf("%d) %s%n", i + 1, wallets.get(i).getPublicKey());
                                    }
                                    System.out.println("0) Exit");
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
                                    while (!password.equals("exit") && !hashEncoder.SHA256(password).equals(wallet.getPassword())) {
                                        password = scanner.nextLine();
                                        System.out.println("Введен неверный пароль!Для выхода из меню ввода пороля введите: 'exit'");
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
                                            if (!asymmetricEncoder.isValidPublicKey(wallet.getPublicKey())) {
                                                break;
                                            }
                                            System.out.println("Введите суммы, которую хотите отправить");
                                            int value = scanner.nextInt();
                                            if (value < 0 || value > balance || balance==0) {
                                                System.out.println("Некорректная сумма или недостаточный баланс");
                                                break;
                                            }
                                            Transaction transaction = Transaction.newTransactionBuilder()
                                                    .setFrom(wallet.getPublicKey())
                                                    .setData("send transaction to "+to)
                                                    .setTo(to).setValue(value)
                                                    .setNonce(javaChainMethodService.getNoncePending(wallet.getPublicKey()))
                                                    .setSing(javaChainMethodService.singTransaction(wallet.getPrivateKey(),wallet.getPublicKey(),to)).build();

                                            if (javaChainMethodService.sendTransaction(transaction)==null){
                                                System.out.println("invalid transaction");
                                                break;
                                            }
                                            System.out.println("Успешно отправленно!");
                                            System.out.println("ждите подтверждение отправки транзакции в блокчейне");
                                            break;
                                        }
                                        //deleteWallet
                                        case 2: {
                                            System.out.println("Введите пароль от кошелька,который хотите удалить");
                                            scanner = new Scanner(System.in);
                                            password = scanner.nextLine();
                                            if (!hashEncoder.SHA256(password).equals(wallet.getPassword())) {
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
                                            if (!hashEncoder.SHA256(password).equals(wallet.getPassword())) {
                                                System.out.println("Неверный пароль");
                                                break;
                                            }
                                            System.out.println("Private Key: " + wallet.getPrivateKey());
                                        }
                                        case 0: {
                                            break;
                                        }
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
                                Asymmetric.Keys keys = asymmetricEncoder.generateKeys();
                                Wallet newWallet = new Wallet(password, keys.publicKey(), keys.privateKey());
                                walletDB.createNewWallet(newWallet);
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
                                        String publicKey = asymmetricEncoder.getPublicFromPrivateKey(privateKey);
                                        if (publicKey == null) {
                                            System.out.println("Неверный privateKey");
                                            break;

                                        }
                                        if (walletDB.getWalletByAddress(publicKey) != null) {
                                            System.out.println("Данный кошелек уже добавлен!");
                                            break;
                                        }
                                        System.out.println("Придумайте новый пароль для своего кошелька,минимум 5 символов");
                                        System.out.println("Или введите exit для выхожа из меню.");
                                        scanner = new Scanner(System.in);
                                        String password = scanner.nextLine();
                                        while (!password.equals("exit") && password.length() < 5) {
                                            System.out.println("Пароль слишком короткий,введите пароль длинной более 5 символов");
                                            password = scanner.nextLine();
                                        }
                                        if (password.equals("exit")) break;
                                        Wallet newWallet = new Wallet(password, publicKey, privateKey);
                                        walletDB.createNewWallet(newWallet);
                                        System.out.println("Кошелек успешно добавлен!");
                                        break;

                                    }
                                }
                            }
                            break;

                        }
                    }
                    break;
                    //mining
                    case 3: {
                        System.out.println("Майнинг запущен,для выхода введите 'exit'");
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
                                e.printStackTrace();
                            }
                        });
                        miningThread.start();
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
                                scanner = new Scanner(System.in);
                                String address = scanner.nextLine();
                                if (!asymmetricEncoder.isValidPublicKey(address)) {
                                    System.out.println("Некорректный аддресс кошелька");
                                }
                                walletDB.setAddressForMining(address);

                            } else {
                                System.out.println("Некорректный номер параметра");
                            }

                            break;
                        }

                    }
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



