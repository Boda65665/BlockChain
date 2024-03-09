package org.example.CustomBlockChain.BlockChain;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import node.entity.Entity;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.BlockChain.BlockChainBase;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.CustomBlockChain.DB.LevelDB.State.LevelDBStateCustom;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;

import org.example.CustomBlockChain.DB.LevelDB.TransactonPool.LevelDbTransactionPool;
import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.BlockChainBase.Exeptions.BlockChainException;

import org.example.CustomBlockChain.NodeCommunication.NodeClient;
import org.example.BlockChainBase.Rules.PoWRule;
import org.example.CustomBlockChain.Rules.TransactionRule;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.*;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    private ArrayList<Transaction> poolTransactions = new ArrayList<>();
    private final Gson gson = new Gson();
    private final BlockChain<ArrayList<Transaction>> blockChain;
    Asymmetric asymmetric = new Asymmetric();
    private final NodeClient nodeClient;
    private final LevelDbTransactionPool levelDbTransactionPool = new LevelDbTransactionPool();
    private final LevelDBStateCustom levelDbState = new LevelDBStateCustom();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    private final LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    private final TransactionRule transactionRule = new TransactionRule();
    PoWRule<ArrayList<Transaction>> poWRule = new PoWRule<>();
    private final HashEncoder hashEncoder = new HashEncoder();



    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain) throws IOException, SQLException, ClassNotFoundException {
        this.blockChain = blockChain;
        poolTransactions = levelDbTransactionPool.getAll();
        nodeClient = new NodeClient(this);
    }

    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) throws Exception {
        if (!poWRule.Execute(block.getBlockNumber(),block.getHash())) {
            System.out.println("Данный блок был найден раньше вас( ");
            return;
        };
        for (Transaction transaction : block.getData()) {
            if (transaction.isStatus()) updateAddressFromTransaction(transaction);
            levelDbTransaction.put(transaction);
        }
        blockChain.addBlock(block);
        //начисляем награду за майнинг
        AddressCustom feeRecipient = (AddressCustom) levelDbState.get(block.getFeeRecipient());
        if (feeRecipient==null) feeRecipient = new AddressCustom().newAddressBuilder()
                .setBalance(0)
                .setNonce(0)
                .setNoncePending(0)
                .setPublicKey(block.getFeeRecipient())
                .setHashTransactionComplete(new ArrayList<>()).build();
        feeRecipient.setBalance(feeRecipient.getBalance()+100);
        levelDbState.update(feeRecipient);
        try {
            nodeClient.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAddressFromTransaction(Transaction transaction) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        AddressCustom to = levelDbState.get(transaction.getTo());
        if (to==null) to = new AddressCustom(asymmetric.generateKeys().publicKey());
        to.setBalance(to.getBalance()+transaction.getValue());
        levelDbState.update(to);
        AddressCustom from = levelDbState.get(transaction.getFrom());
        from.setBalance(from.getBalance()-transaction.getValue());
        ArrayList<String>transactionsCompletedAddressFrom = from.getTransactionsComplete();
        if (transactionsCompletedAddressFrom==null) transactionsCompletedAddressFrom=new ArrayList<>();
        transactionsCompletedAddressFrom.add(transaction.getHash());
        from.setTransactionsComplete(transactionsCompletedAddressFrom);
        if (!transaction.getFrom().equals(transaction.getTo())) {
            ArrayList<String>transactionsCompletedAddressTo = to.getTransactionsComplete();
            transactionsCompletedAddressTo.add(transaction.getHash());
        }
        levelDbState.update(to);
        levelDbState.update(from);
    }
    @Override
    public ArrayList<Block<ArrayList<Transaction>>> getBlocksStartingFrom(int numberBlock){
        return new ArrayList<>(getBlocks().subList(numberBlock, getBlocks().size()));
    }

    public ArrayList<Block<ArrayList<Transaction>>> getBlocks(){
        return blockChain.getBlocks();
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
    public String addTransactionToPoolTransactions(Transaction transaction) throws Exception {;
        if (transactionRule.Execute(transaction)) {
            Address address = levelDbState.get(transaction.getFrom());
            address.setNoncePending(address.getNoncePending()+1);

            transaction.setHash(hashEncoder.SHA256(transaction.getFrom()+transaction.getValue()+address.getNoncePending()));
            poolTransactions.add(transaction);
            levelDbState.update(address);
            levelDbTransactionPool.put(transaction);

           // nodeClient.update();
            return transaction.getHash();
        }

        return null;

    }

    public void clearPoolTransaction() throws IOException {
        poolTransactions.clear();
        levelDbTransactionPool.clear();
    }

    public void addBlockToPoll(Block<ArrayList<Transaction>> block) throws Exception {
        blockChain.addBlockToPoll(block);
        //nodeClient.update();
    }

    @Override
    public boolean isValid(ArrayList<Block<ArrayList<Transaction>>> blocks) throws BlockChainException, IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {

        if (!blockChain.isValid(blocks)) return false;
        for (Block<ArrayList<Transaction>> block : blocks) {
            if (!transactionRule.validTransactions(block.getData())) return false;
        }
        return true;
    }

    @Override
    public boolean isQueryValid(String lastHash, int height, BlockChainInfoBD.BlockChainInfoStruct actualInfoBlockChain) {
        return blockChain.isQueryValid(lastHash,height,actualInfoBlockChain);
    }

    @Override
    public void scanBlockChain() throws Exception {
        blockChain.scanBlockChain();
    }

    @Override
    public void addAll(ArrayList<Block<ArrayList<Transaction>>> blocks) throws Exception {
        for (Block<ArrayList<Transaction>> block : blocks) {
            addBlock(block);
        }
    }

    @Override
    public void addAllToBlockPoll(ArrayList<Block<ArrayList<Transaction>>> blocks) throws Exception {
        for (Block<ArrayList<Transaction>> block : blocks) {
            addBlockToPoll(block);
        }
    }
    public void addAllTransactionToPoolTransactions(ArrayList<Transaction> transactions) throws Exception {
        for (Transaction transaction : transactions) {
            addTransactionToPoolTransactions(transaction);
        }
    }


    public void setPoolTransactions(ArrayList<Transaction> poolTransactions) throws IOException {
        this.poolTransactions = poolTransactions;
        levelDbTransactionPool.buildTransactionPool(gson.toJson(poolTransactions));
    }

    public ArrayList<Transaction> getPoolTransactions() {
        return poolTransactions;
    }
    @Override
    public String getTailFromBlockPoll(){
        return blockChain.getTailFromBlockPoll();
    }

    public int getBlockNumberFromBlockPool(){
        return blockChain.getBlockNumberFromBlockPool();
    }


    public boolean isValidPools(List<Entity.Block> blocksPool, List<Entity.Transaction> transactions) {
        return true;
    }
}
