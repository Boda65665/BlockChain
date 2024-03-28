package org.example.CustomBlockChain.BlockChain;

import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.BlockChain.BlockChainBase;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
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
import org.example.CustomBlockChain.Rules.TransactionRule;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.*;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    final private ArrayList<Transaction> poolTransactions;
    private final BlockChain<ArrayList<Transaction>> blockChain;
    Asymmetric asymmetric = new Asymmetric();
    private final NodeClient nodeClient;
    private final LevelDbTransactionPool levelDbTransactionPool = new LevelDbTransactionPool();
    private final LevelDBStateCustom levelDbState = new LevelDBStateCustom();
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    private final TransactionRule transactionRule = new TransactionRule();
    private final HashEncoder hashEncoder = new HashEncoder();
    BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    Logger logger = Logger.getLogger("JavaChain");


    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain) throws IOException, SQLException, ClassNotFoundException {
        this.blockChain = blockChain;
        poolTransactions = levelDbTransactionPool.getAll();
        nodeClient = new NodeClient(this);
    }

    @Override

    public void addAll(ArrayList<Block<ArrayList<Transaction>>> blocks) {
        for (Block<ArrayList<Transaction>> block : blocks) {
            addBlock(block);
        }
    }
    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) {
        try {
            blockChain.addBlock(block);
            for (Transaction transaction : block.getData()) {
                if (transaction.isStatus()) updateAddressFromTransaction(transaction);
                AddressCustom from = levelDbState.get(transaction.getFrom());
                from.setNonce(from.getNoncePending());
                levelDbState.update(from);
                levelDbTransaction.put(transaction);
            }
            awardRewardsForMining(block.getFeeRecipient());
            nodeClient.update(block);
        } catch (Exception e) {
            logger.log(Level.WARNING,"Block not adder so block is invalid");
            e.printStackTrace();

        }
    }
    public void awardRewardsForMining(String addressFeeRecipient) throws IOException {
        AddressCustom feeRecipient = levelDbState.get(addressFeeRecipient);
        if (feeRecipient==null) feeRecipient = new AddressCustom().newAddressBuilder()
                .setBalance(0)
                .setNonce(0)
                .setNoncePending(0)
                .setPublicKey(addressFeeRecipient)
                .setHashTransactionComplete(new ArrayList<>()).build();
        feeRecipient.setBalance(feeRecipient.getBalance()+100);
        levelDbState.update(feeRecipient);
    }

    public void updateAddressFromTransaction(Transaction transaction) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        AddressCustom to = levelDbState.get(transaction.getTo());
        if (to==null) to = new AddressCustom(asymmetric.generateKeys().publicKey());
        to.setBalance(to.getBalance()+transaction.getValue());
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

    public String addTransactionToPoolTransactions(Transaction transaction) throws Exception {
        if (transactionRule.Execute(transaction)) {
            Address address = levelDbState.get(transaction.getFrom());
            transaction.setHash(hashEncoder.SHA256(transaction.getFrom()+transaction.getValue()+address.getNoncePending()));
            transaction.setNonce(address.getNoncePending());
            address.setNoncePending(address.getNoncePending()+1);
            poolTransactions.add(transaction);
            levelDbState.update(address);
            levelDbTransactionPool.put(transaction);
           nodeClient.update(transaction);
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
    }

    @Override
    public boolean isValid(Block<ArrayList<Transaction>> block,String tail) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        if (!blockChain.isValid(block,tail)) return false;
        return transactionRule.validTransactions(block.getData());
    }

    @Override
    public boolean isQueryValid(String lastHash, int height, BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct) {
        return blockChain.isQueryValid(lastHash,height,blockChainInfoStruct);
    }


    @Override
    public void scanBlockChain() throws Exception {
        blockChain.scanBlockChain();
    }

    @Override
    public boolean isValidAllBlock(ArrayList<Block<ArrayList<Transaction>>> blocks) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        for (Block<ArrayList<Transaction>> block : blocks) if(!isValid(block,block.getParentHash())) return false;
        return true;
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
    //if transaction already invalid than update data block and hash
    public Block<ArrayList<Transaction>> updateBlockInformation(Block<ArrayList<Transaction>> block) throws IOException {
        block.setBlockNumber(blockChain.getBlocks().size() + 1);
        block.setParentHash(blockChain.getTail());
        for (Transaction transaction : block.getData()) {
            transaction.setBlockNumber(block.getBlockNumber());
            if (transactionRule.Execute(transaction)) transaction.setStatus(true);
        }
        block.setHash(Block.calculateHash(block.getData(), blockChain.getTail(), hashEncoder, block.getNonce()));
        return block;
    }


    public boolean isValidPools(ArrayList<Block<ArrayList<Transaction>>> blocksPool, ArrayList<Transaction> transactions) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, BlockChainException {
        return isValidAllBlock(blocksPool) && transactionRule.validTransactions(transactions);
    }
    @Override
    public boolean isAlreadyExistBlock(int blockNumber) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return blockChain.isAlreadyExistBlock(blockNumber);
    }

    @Override
    public boolean isAlreadyExistBlockPending(int blockNumber) throws Exception {
        return blockChain.isAlreadyExistBlockPending(blockNumber);
    }

    public boolean isAlreadyExistTransactionPending(String hashTransaction) throws IOException {
        return levelDbTransaction.get(hashTransaction)!=null || levelDbTransactionPool.get(hashTransaction)!=null;
    }

}
