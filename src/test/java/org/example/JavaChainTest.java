package org.example;

import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.BlockChain.JavaChain;
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
import java.util.ArrayList;

public class JavaChainTest {
    HashEncoder hashEncoder = new HashEncoder();
    JavaChain javaChain = new JavaChain(new BlockChain<>(hashEncoder));
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);

    public JavaChainTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void isValid() throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        ArrayList<Block<ArrayList<Transaction>>> blocks = levelDbBlock.getAll();
        blocks.add(null);
        for (Block<ArrayList<Transaction>> block : blocks) {
            Assertions.assertTrue(javaChain.isValid(block,block.getParentHash()));

        }
    }
}
