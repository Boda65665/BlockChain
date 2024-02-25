package org.example.BlockChainBase.Rules;

import org.example.BlockChainBase.Entity.Block;

import java.util.ArrayList;

public class PoWRule<T>{

    public boolean Execute(int height, String hash)
    {

        int complexity = (int)(Math.log(height + 1) + 1);
        complexity = 2;
        for (int i = 0; i < complexity; i++)
        {
            if (hash.charAt(i) != '0')
                return false;
        }

        return true;
    }


    public boolean Execute(ArrayList<Block<T>> blocks, Block<T> newBlock) {
        var height = blocks.size();

        return Execute(height, newBlock.getHash());
    }
}
