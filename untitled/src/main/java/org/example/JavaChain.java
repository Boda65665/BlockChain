package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.Rules.TransactionRule;
import org.example.Service.BuildBlockChainService;

import java.util.ArrayList;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    private final BlockChain<ArrayList<Transaction>> blockChain;
    private final BuildBlockChainService<ArrayList<Transaction>> buildBlockService;
    private final HashEncoder hashEncoder = new HashEncoder();
    TransactionRule transactionRule = new TransactionRule();

//    private ArrayList<R>


    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain, BuildBlockChainService<ArrayList<Transaction>> buildBlockService) {
        this.blockChain = blockChain;
        this.buildBlockService = buildBlockService;
    }

    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) throws JsonProcessingException, BlockChainException {
        ArrayList<Transaction> transactions = transactionRule.executeTransactions(blockChain.getBlocks(),block);
        block.setData(transactions);
        blockChain.addBlock(block);
    }

    public void addBlock(ArrayList<Transaction> transactions) throws JsonProcessingException, BlockChainException {
        addBlock(new Block<>(blockChain.getTail(),Block.calculateHash(transactions,blockChain.getTail(),hashEncoder),blockChain.getBlockNumber(),transactions));

    }



}
