package org.example;

import com.google.gson.reflect.TypeToken;
import org.checkerframework.checker.units.qual.A;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Exeptions.BlockChainException;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.CustomBlockChain.DB.LevelDB.TransactonPool.LevelDbTransactionPool;
import org.example.CustomBlockChain.Entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class JavaChainTest {
    HashEncoder hashEncoder = new HashEncoder();
    BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(hashEncoder);
    JavaChain javaChain = new JavaChain(blockChain);
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);
    LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock  = new LevelDbPoolBlock<>(typeData);
    LevelDbTransactionPool levelDbTransactionPool = new LevelDbTransactionPool();

    public JavaChainTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void isValid() throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        ArrayList<Block<ArrayList<Transaction>>> blocks = levelDbBlock.getAll();
        blocks.add(null);
        for (Block<ArrayList<Transaction>> block : blocks) {
            Assertions.assertTrue(javaChain.isValid(block, block.getParentHash()));

        }
    }
        @Test
        void isValidPending() throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
            ArrayDeque<Block<ArrayList<Transaction>>> blocks = levelDbPoolBlock.getAll();
            for (Block<ArrayList<Transaction>> block : blocks) {
                Assertions.assertTrue(javaChain.isValid(block,block.getParentHash()));
            }
        }
        @Test
        void isValidPool() throws BlockChainException, IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
            ArrayList<Block<ArrayList<Transaction>>> blocks = levelDbBlock.getAll();
            ArrayList<Transaction> transactions = levelDbTransaction.getAll();
            Assertions.assertTrue(javaChain.isValidPools(new ArrayList<>(blocks),transactions));
            transactions.getFirst().setHash("3432");
            Assertions.assertFalse(javaChain.isValidPools(new ArrayList<>(blocks),transactions));
        }
    }

