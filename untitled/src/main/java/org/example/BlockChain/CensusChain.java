package org.example.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChain.BlockChain;
import org.example.BlockChain.BlockChainBase;
import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;
import org.example.Rules.CensusRule;

public class CensusChain implements BlockChainBase<String>
{
    private final BlockChain<String> blockChain;
    private final CensusRule censusRule = new CensusRule();
    public CensusChain(BlockChain<String> blockChain) {
        this.blockChain = blockChain;
    }

    @Override
    public void addBlock(Block<String> block) throws JsonProcessingException, BlockChainException {
        censusRule.Execute(blockChain.getBlocks(),block);
        blockChain.addBlock(block);


    }
    public String toString(){
        String blockString = "";
        for (Block<String> block : blockChain.getBlocks()) {
            blockString += "Name: "+block.getData()+"\n";



        }
        return blockString;
    }
}
