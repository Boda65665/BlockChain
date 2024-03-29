package org.example.CustomBlockChain.Servise;

import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.CustomBlockChain.DB.LevelDB.State.LevelDBStateCustom;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.TypeDownloadRequestNodeCommunication;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;


public class JavaChainMethodService {
    LevelDBStateCustom levelDbState = new LevelDBStateCustom();
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    Asymmetric asymmetric = new Asymmetric();


    HashEncoder hashEncoder = new HashEncoder();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>() {
    }.getType();
    BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(hashEncoder);
    LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);
    JavaChain javaChain = new JavaChain(blockChain);

    public JavaChainMethodService() throws IOException, SQLException, ClassNotFoundException {
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
    public String singTransaction(String privateKey, String publicKey, String to) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException {
        return asymmetric.sign(hashEncoder.SHA256(to + getNoncePending(publicKey)), privateKey);
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
    public Block<ArrayList<Transaction>> updateBlockInformation(Block<ArrayList<Transaction>> block) throws IOException {
        return javaChain.updateBlockInformation(block);
    }

}
