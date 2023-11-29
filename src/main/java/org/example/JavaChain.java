package org.example;

import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.Rules.PoWRule;
import org.example.Rules.TransactionRule;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    private ArrayList<Transaction> poolTransactions = new ArrayList<>();
    private final BlockChain<ArrayList<Transaction>> blockChain;
    private final LevelDbState levelDbState = new LevelDbState();
    private final LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>();


    private final TransactionRule transactionRule = new TransactionRule();
    PoWRule<ArrayList<Transaction>> poWRule = new PoWRule<>();
    private final HashEncoder hashEncoder = new HashEncoder();



    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain) throws IOException {
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
                from.setNonce(transaction.getNonce());
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
    public boolean addTransactionToPoolTransactions(Transaction transaction) throws IOException {

        if (transactionRule.Execute(transaction)) {
            poolTransactions.add(transaction);
            if (poolTransactions.size()==4) {
                ArrayList<Transaction> dataBlock = new ArrayList<>(poolTransactions);
                Block<ArrayList<Transaction>> newBlock = new Block<>(dataBlock);
                poolTransactions.clear();
                addBlockToPoll(newBlock);
            }
            return true;
        }
        return false;



    }
    public void addBlockToPoll(Block<ArrayList<Transaction>> block) throws IOException {
        blockChain.addBlockToPoll(block);
    }
    public Block<ArrayList<Transaction>> popBlockFromPool() throws IOException {
        return blockChain.getBlockFromPoll();
    }

    public void setPoolTransactions(ArrayList<Transaction> poolTransactions) {
        this.poolTransactions = poolTransactions;
    }

    public ArrayList<Transaction> getPoolTransactions() {
        return poolTransactions;
    }
}
