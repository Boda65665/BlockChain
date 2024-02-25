package org.example.CustomBlockChain.Servise;

import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;

import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.Entity.Address;
import org.example.CustomBlockChain.DB.LevelDB.State.LevelDBStateCustom;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.BlockChainBase.DB.SQL.Node.IpConfigParser;
import org.example.CustomBlockChain.NodeCommunication.NodeClient;
import org.example.CustomBlockChain.Rules.TransactionRule;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Objects;

public class JavaChainMethodService {
    LevelDBStateCustom levelDbState = new LevelDBStateCustom();
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    Asymmetric asymmetric = new Asymmetric();
    TransactionRule transactionRule = new TransactionRule();
    final IpConfigParser ipConfigParser = new IpConfigParser();
    final String ipAddress = ipConfigParser.getIpAddress();
    NodeListDB nodeListDB = new NodeListDB();
    HashEncoder hashEncoder = new HashEncoder();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(hashEncoder);
    LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);
    JavaChain javaChain = new JavaChain(blockChain);
    NodeClient nodeClient = new NodeClient(javaChain);

    public JavaChainMethodService() throws IOException, SQLException, ClassNotFoundException {
    }
    public JavaChainMethodService(BlockChain<ArrayList<Transaction>> blockChain,JavaChain javaChain) throws IOException, SQLException, ClassNotFoundException {
        this.blockChain=blockChain;
        this.javaChain=javaChain;
    }



    public AddressCustom getAddress(String address) throws IOException {
        return levelDbState.get(address);
    }

    public Block<ArrayList<Transaction>> getBlockByHash(String hash) throws IOException {
        return levelDbBlock.getByHash(hash);
    }

    public Block<ArrayList<Transaction>> getBlockByNumber(long number) throws IOException {
        return levelDbBlock.getByNumber(number);
    }

    public JavaChain getJavaChain() {
        return javaChain;
    }

    public BlockChain<ArrayList<Transaction>> getBlockChain() {
        return blockChain;
    }

    public String sendTransaction(Transaction transaction) throws Exception {

        return javaChain.addTransactionToPoolTransactions(transaction);
    }

    public ArrayDeque<Block<ArrayList<Transaction>>> getBlocksPoll() {
        return blockChain.getBlocksPool();
    }

    public int getBlockNumber() {
        return blockChain.getBlockNumber();
    }

    public boolean synchronizationBlockChain() throws Exception {
        String randomIpNode = nodeListDB.getRandomIp();
        while (randomIpNode != null && !nodeClient.SynchronizationBlockChain(randomIpNode,getBlockNumber())) {
            randomIpNode = nodeListDB.getRandomIp();
        }
        if (randomIpNode == null) {
            nodeListDB.editStatusActive(ipAddress, true);
            return false;
        }
        return true;

    }

    public String singTransaction(String privateKey, String publicKey, String to) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException {
        return asymmetric.sign(hashEncoder.SHA256(to+getNoncePending(publicKey)),privateKey);
    }

    public int getTransactionNonce(String address) throws IOException {
        return levelDbState.get(address).getNonce();
    }
    public int getNoncePending(String address) throws IOException {
        return levelDbState.get(address).getNoncePending();

    }

    public Transaction getTransaction(String hash) throws IOException {
        return levelDbTransaction.get(hash);
    }

    public Block<ArrayList<Transaction>> popBlockFromPoll() throws IOException {
        return blockChain.popBlockFromPoll();
    }
    //if transaction already invalid than update data block and hash
    public Block<ArrayList<Transaction>> updateBlock(Block<ArrayList<Transaction>> block) throws IOException {
        block.setBlockNumber(blockChain.getBlocks().size()+1);
        block.setParentHash(blockChain.getTail());
        for (Transaction transaction : block.getData()) {
            AddressCustom from = transaction.getFrom();
            AddressCustom to = transaction.getTo();

            if (levelDbState.get(to.getPublicKey()) != null)
                to.setBalance(levelDbState.get(to.getPublicKey()).getBalance());
            if (levelDbState.get(from.getPublicKey()) == null) continue;
            transaction.setBlockNumber(block.getBlockNumber());
            from.setBalance(levelDbState.get(from.getPublicKey()).getBalance());
            if (transactionRule.Execute(transaction)) {
                if (!from.getPublicKey().equals(to.getPublicKey())) {

                }

                transaction.setStatus(true);
                AddressCustom fromAddressCustom = levelDbState.get(from.getPublicKey());
                AddressCustom toAddressCustom =   levelDbState.get(to.getPublicKey());
                ArrayList<String> fromTransactionsComplete = fromAddressCustom.getTransactionsComplete();
                ArrayList<String> toTransactionsComplete = toAddressCustom.getTransactionsComplete();
                if (fromTransactionsComplete == null) fromTransactionsComplete = new ArrayList<>();
                fromTransactionsComplete.add(transaction.getHash());
                if (!to.getPublicKey().equals(from.getPublicKey())) {
                    if (toTransactionsComplete == null) toTransactionsComplete = new ArrayList<>();
                    toTransactionsComplete.add(transaction.getHash());
                    to.setTransactionsComplete(toTransactionsComplete);
                    from.setBalance(levelDbState.get(from.getPublicKey()).getBalance() - transaction.getValue());
                    to.setBalance(to.getBalance() + transaction.getValue());
                }
                from.setTransactionsComplete(fromTransactionsComplete);
            }

            from.setNonce(transaction.getNonce() + 1);
            from.setNoncePending(levelDbState.get(from.getPublicKey()).getNoncePending());
            transaction.setFrom(from);
            transaction.setTo(to);
        }

        block.setHash(Block.calculateHash(block.getData(),blockChain.getTail(),hashEncoder,block.getNonce()));
        return block;
    }

    public static void main(String[] args) throws SQLException, IOException, ClassNotFoundException {
        JavaChainMethodService javaChainMethodService = new JavaChainMethodService();

        System.out.println(javaChainMethodService.getTransaction("b128187d315d52b5452f573caa4d2926325f9166e0eac7a16baada839600cd75"));
    }
}
