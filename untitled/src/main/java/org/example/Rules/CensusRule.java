package org.example.Rules;

import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public class CensusRule implements RuleBase<String>{






    @Override
    public void Execute(ArrayList<Block<String>> blocks, Block<String> newBlock) throws BlockChainException {
        if (blocks.stream().anyMatch(block -> block.getData().equals(newBlock.getData()))) {
            throw new BlockChainException("duplicateName");
        }

    }
}
