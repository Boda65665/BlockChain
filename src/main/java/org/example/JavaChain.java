package org.example;

import com.google.gson.reflect.TypeToken;
import org.checkerframework.checker.units.qual.A;
import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
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
    private final BlockChain<ArrayList<Transaction>> blockChain;
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

    }

    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) throws IOException, BlockChainException {
        poWRule.Execute(getBlocks(),block);

        block.setBlockNumber(blockChain.getBlockNumber());
        block.setParentHash(blockChain.getTail());

        for (Transaction transaction : block.getData()) {
            Address from = transaction.getFrom();
            Address to = transaction.getTo();
            if (levelDbState.get(from.getPublicKey())==null) throw new BlockChainException("Данного кошелька не существует!");
            if (levelDbState.get(to.getPublicKey())==null){
                levelDbState.put(to);
            }

            if (transactionRule.Execute(transaction)){
                from.setBalance(from.getBalance()-transaction.getValue());
                from.setNonce(transaction.getNonce()+1);
                transaction.setStatus(true);
                to.setBalance(to.getBalance()+transaction.getValue());
                from.getTransactionsComplete().add(transaction);
                to.getTransactionsComplete().add(transaction);
                levelDbState.update(to);
                levelDbState.update(from);
            }
            transaction.setBlockNumber(block.getBlockNumber());

        }
        block.setHash(Block.calculateHash(block.getData(),blockChain.getTail(),hashEncoder,block.getNonce()));
        blockChain.addBlock(block);
        levelDbBlock.put(block);
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
                Block<ArrayList<Transaction>> newBlock = new Block<>(dataBlock);
                poolTransactions.clear();
                addBlockToPoll(newBlock);
            }
            nodeJavaChainClient.update();
            return true;
        }
        return false;



    }
    public void addBlockToPoll(Block<ArrayList<Transaction>> block) throws Exception {
        blockChain.addBlockToPoll(block);
        nodeJavaChainClient.update();
    }


    public void setPoolTransactions(ArrayList<Transaction> poolTransactions) {
        this.poolTransactions = poolTransactions;
    }

    public ArrayList<Transaction> getPoolTransactions() {
        return poolTransactions;
    }
    public Transaction buildTransaction(String fromAddressString,String toAddressString,String privateKey,int value) throws UnsupportedEncodingException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException {
        Address fromAddress = new Address(fromAddressString);
        Address toAddress = new Address(toAddressString);
        String sign = asymmetric.sign(hashEncoder.SHA256(toAddressString+fromAddress.getNonce()),privateKey);
        return new Transaction(fromAddress,0,0,sign,toAddress,value,"",fromAddress.getNonce());
    }
}
