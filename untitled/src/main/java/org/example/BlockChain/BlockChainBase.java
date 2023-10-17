package org.example.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

public interface BlockChainBase<T> {
    public void addBlock(Block<T> block)throws JsonProcessingException, BlockChainException;
}
