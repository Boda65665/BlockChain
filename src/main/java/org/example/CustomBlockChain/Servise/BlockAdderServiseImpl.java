package org.example.CustomBlockChain.Servise;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Entity.BlockType;
import org.example.BlockChainBase.Service.BlockAdderServise;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BlockAdderServiseImpl implements BlockAdderServise
{
    private final JavaChain javaChain;
    private final HashEncoder hashEncoder = new HashEncoder();
    public BlockAdderServiseImpl(JavaChain javaChain) {
        this.javaChain = javaChain;
    }
    @Override
    public void run() throws Exception {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        java.time.LocalTime currentTimeUTC = java.time.LocalTime.now(java.time.ZoneOffset.UTC);
        int milliseconds = currentTimeUTC.getNano() / 1_000_000; // Получаем миллисекунды из наносекунд
        // Вычисляем время до первого вызова(следующей минуты)
        long initialDelay = 60000 - (currentTimeUTC.getSecond()*1000+milliseconds);
        scheduler.scheduleAtFixedRate(() -> {

            ArrayList<Transaction> dataBlock = new ArrayList<>(javaChain.getPoolTransactions());
            Block<ArrayList<Transaction>> newBlock = null;
            try {

                newBlock = buildBlockPending(dataBlock);
                javaChain.clearPoolTransaction();
                javaChain.addBlockToPoll(newBlock);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, initialDelay, 60000, TimeUnit.MILLISECONDS);
//                    }, 100, 1000, TimeUnit.MILLISECONDS);


        }
    public Block<ArrayList<Transaction>> buildBlockPending(ArrayList<Transaction> data) throws JsonProcessingException {
        Block<ArrayList<Transaction>> block = new Block<>(data);
        block.setBlockNumber(javaChain.getBlockNumberFromBlockPool());
        block.setParentHash(javaChain.getTailFromBlockPoll());
        block.setNonce(-1);
        block.setHash(Block.calculateHash(data,block.getParentHash(),hashEncoder,block.getNonce()));
        block.setBlockType(BlockType.PENDING);
        return block;
    }
}
