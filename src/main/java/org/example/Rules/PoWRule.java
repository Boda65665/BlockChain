package org.example.Rules;

import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public class PoWRule<T> implements RuleBase<T>{

    public boolean Execute(int height, String hash)
    {

        int complexity = (int)(Math.log(height + 1) + 1);
        complexity = 1;
        for (int i = 0; i < complexity; i++)
        {
            if (hash.charAt(i) != '0')
                return false;
        }

        return true;
    }

    @Override
    public void Execute(ArrayList<Block<T>> blocks, Block<T> newBlock) throws BlockChainException {
        var height = blocks.size();
        
        if (!Execute(height, newBlock.getHash()))
            throw new BlockChainException("Proof of work is incorrect for this block.");
    }
}
