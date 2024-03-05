package org.example;

import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Entity.Transaction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.converter.ConvertWith;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class LevelDbPoolTransactionTest {
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    @Test
    void getAll() throws IOException {
        System.out.println(levelDbPoolBlock.getAll());
        Assertions.assertFalse(levelDbPoolBlock.getAll().isEmpty());

    }
    @Test
    void get() throws IOException {
        for (Block<ArrayList<Transaction>> block : levelDbPoolBlock.getAll()) {
            levelDbPoolBlock.get(block.getHash());
            Assertions.assertNotNull(levelDbPoolBlock.get(block.getHash()));

        }
    }

    }
