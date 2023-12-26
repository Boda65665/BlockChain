package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.checkerframework.checker.units.qual.A;
import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
import org.example.DB.LevelDb.TransactonPool.LevelDbTransactionPool;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.NodeCommunication.JavaChainNode.NodeJavaChainClient;
import org.example.NodeCommunication.JavaChainNode.NodeJavaChainServer;
import org.example.Rules.PoWRule;
import org.example.Rules.TransactionRule;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    private ArrayList<Transaction> poolTransactions = new ArrayList<>();
    private final Gson gson = new Gson();
    private final BlockChain<ArrayList<Transaction>> blockChain;
    private final LevelDbTransactionPool levelDbTransactionPool = new LevelDbTransactionPool();
    private final LevelDbState levelDbState = new LevelDbState();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    private final LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);

    private final Asymmetric asymmetric = new Asymmetric();
    private final TransactionRule transactionRule = new TransactionRule();
    PoWRule<ArrayList<Transaction>> poWRule = new PoWRule<>();
    private final HashEncoder hashEncoder = new HashEncoder();

    private final NodeJavaChainClient nodeJavaChainClient = new NodeJavaChainClient(this);


    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain) throws IOException, SQLException {
        this.blockChain = blockChain;
        poolTransactions=levelDbTransactionPool.getAll();

    }

    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) throws Exception {
        Address address = new Address("dw");

        if (!poWRule.Execute(block.getBlockNumber(),block.getHash())) {
            System.out.println("Данный блок был найден раньше вас( ");
            return;
        };

        block.setBlockNumber(blockChain.getBlockNumber());
        block.setParentHash(blockChain.getTail());

        for (Transaction transaction : block.getData()) {

            Address from = transaction.getFrom();
            Address to = transaction.getTo();
            from.setBalance(levelDbState.get(from.getPublicKey()).getBalance());
            if (levelDbState.get(to.getPublicKey())!=null) to.setBalance(levelDbState.get(to.getPublicKey()).getBalance());

            if (levelDbState.get(from.getPublicKey())==null) throw new BlockChainException("Данного кошелька не существует!");
            if (levelDbState.get(to.getPublicKey())==null){
                levelDbState.put(to);
            }

            if (transactionRule.Execute(transaction)){
                from.setBalance(from.getBalance()-transaction.getValue());
                from.setNonce(transaction.getNonce()+1);
                transaction.setStatus(true);

                to.setBalance(to.getBalance()+transaction.getValue());
                ArrayList<String> fromTransactionsComplete = from.getTransactionsComplete();
                ArrayList<String> toTransactionsComplete = to.getTransactionsComplete();

                if (fromTransactionsComplete==null) fromTransactionsComplete=new ArrayList<>();
                if (toTransactionsComplete==null) toTransactionsComplete=new ArrayList<>();

                fromTransactionsComplete.add(transaction.getHash());
                toTransactionsComplete.add(transaction.getHash());
                from.setTransactionsComplete(fromTransactionsComplete);
                to.setTransactionsComplete(toTransactionsComplete);
                levelDbState.update(to);
                levelDbState.update(from);

            }

            transaction.setBlockNumber(block.getBlockNumber());

        }
        block.setHash(Block.calculateHash(block.getData(),blockChain.getTail(),hashEncoder,block.getNonce()));
        blockChain.addBlock(block);
        levelDbBlock.put(block);
        //начисляем награду за майнинг
        Address feeRecipient = levelDbState.get(block.getFeeRecipient());
        if (feeRecipient==null) feeRecipient = new Address(block.getFeeRecipient(),0,null,0);
        feeRecipient.setBalance(feeRecipient.getBalance()+100);
        levelDbState.update(feeRecipient);
        try {
            nodeJavaChainClient.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<Block<ArrayList<Transaction>>> getBlocks(){
        return blockChain.getBlocks();
    }

    @Override
    public ArrayList<Block<ArrayList<Transaction>>> getBlocksStartingFrom(String hash) {
        return blockChain.getBlocksStartingFrom(hash);
    }

    @Override
    public ArrayList<Address> getAddressStartingFrom(String publicKey) {
        return blockChain.getAddressStartingFrom(publicKey);
    }

    @Override
    public ArrayDeque<Block<ArrayList<Transaction>>> getBlocksPool() {
        return blockChain.getBlocksPool();
    }

    @Override
    public ArrayList<Address> getAllAddresses() {
        return blockChain.getAllAddresses();
    }

    public Block<ArrayList<Transaction>> getLastBLock(){
        ArrayList<Block<ArrayList<Transaction>>> blocks = blockChain.getBlocks();
        return blocks.get(blocks.size()-1);
    }
    public boolean addTransactionToPoolTransactions(Transaction transaction) throws Exception {

        if (transactionRule.Execute(transaction)) {
            poolTransactions.add(transaction);
            if (poolTransactions.size()==1) {
                ArrayList<Transaction> dataBlock = new ArrayList<>(poolTransactions);
                Block<ArrayList<Transaction>> newBlock = buildBlock(dataBlock);
                clearPoolTransaction();
                addBlockToPoll(newBlock);
            }
            nodeJavaChainClient.update();
            return true;
        }
        return false;



    }

    private void clearPoolTransaction() throws IOException {
        poolTransactions.clear();
        levelDbTransactionPool.clear();
    }

    public void addBlockToPoll(Block<ArrayList<Transaction>> block) throws Exception {
        blockChain.addBlockToPoll(block);
        nodeJavaChainClient.update();
    }


    public void setPoolTransactions(ArrayList<Transaction> poolTransactions) throws IOException {
        this.poolTransactions = poolTransactions;
        levelDbTransactionPool.buildTransactionPool(gson.toJson(poolTransactions));
    }
    private String getTail(){
        return blockChain.getTail();
    }

    public ArrayList<Transaction> getPoolTransactions() {
        return poolTransactions;
    }
    public Transaction buildTransaction(String fromAddressString,String toAddressString,String privateKey,int value) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException {
        Address fromAddress = levelDbState.get(fromAddressString);
        Address toAddress = new Address(toAddressString);
        String sign = asymmetric.sign(hashEncoder.SHA256(toAddressString+fromAddress.getNonce()),privateKey);
        String hash = fromAddress.getPublicKey()+fromAddress.getNonce();
        return new Transaction(fromAddress,0,0,sign,toAddress,value,"",fromAddress.getNonce(),hash);
    }
    public Block<ArrayList<Transaction>> buildBlock(ArrayList<Transaction> data) throws JsonProcessingException {
        Block<ArrayList<Transaction>> block = new Block<>(data);

        block.setHash(Block.calculateHash(block.getData(),getTail(),hashEncoder,block.getNonce()));
        return block;
    }
}
