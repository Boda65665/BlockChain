package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.Service.BuildBlockChainService;
import org.example.Validations.RuleTransaction;

import java.util.ArrayList;

public class JavaChain implements BlockChainBase<ArrayList<Transaction>> {
    private final BlockChain<ArrayList<Transaction>> blockChain;
    private final BuildBlockChainService<ArrayList<Transaction>> buildBlockService;
    private final HashEncoder hashEncoder = new HashEncoder();
    RuleTransaction ruleTransaction = new RuleTransaction();
//    private ArrayList<R>


    public JavaChain(BlockChain<ArrayList<Transaction>> blockChain, BuildBlockChainService<ArrayList<Transaction>> buildBlockService) {
        this.blockChain = blockChain;
        this.buildBlockService = buildBlockService;
    }

    @Override
    public void addBlock(Block<ArrayList<Transaction>> block) throws JsonProcessingException, BlockChainException {
        ruleTransaction.Execute(blockChain.getBlocks(),block);
        block.setHash(Block.calculateHash(block.getData(),blockChain.getTail(),hashEncoder));
        blockChain.addBlock(block);
    }

    public void addBlock(ArrayList<Transaction> transactions) throws JsonProcessingException, BlockChainException {
        addBlock(new Block<>(blockChain.getTail(),Block.calculateHash(transactions,blockChain.getTail(),hashEncoder),blockChain.getBlockNumber(),transactions));

    }
    public ArrayList<Block<ArrayList<Transaction>>> getBlocks(){
        return blockChain.getBlocks();
    }
    public Block<ArrayList<Transaction>> getLastBLock(){
        return blockChain.getBlocks().getLast();
    }


}
