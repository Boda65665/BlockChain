package org.example;

import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.Service.BlockAdderServise;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Servise.BlockAdderServiseImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class BlockAdderServiseTest {
    BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(new HashEncoder());
    JavaChain javaChain = new JavaChain(blockChain);
    BlockAdderServise blockAdderServise = new BlockAdderServiseImpl(javaChain);
    public BlockAdderServiseTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void run() throws Exception {
        int startSizePoolBlock = javaChain.getBlockNumberFromBlockPool();
        blockAdderServise.run();
        int endSizePoolBlock = javaChain.getBlockNumberFromBlockPool();
        Assertions.assertTrue(startSizePoolBlock<endSizePoolBlock);
    }
}
