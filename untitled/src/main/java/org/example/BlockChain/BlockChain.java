package org.example.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;
import org.example.Cryptography.HashEncoder;

import java.util.ArrayList;

public class BlockChain<T> implements BlockChainBase<T>{
    private final HashEncoder hashEncoder;
    String tail = null;
    int blockNumber = 0;
    private final ArrayList<Block<T>> blocks = new ArrayList<>();

    public BlockChain(HashEncoder hashEncoder) {
        this.hashEncoder = hashEncoder;
    }
    public void addBlock(Block<T> block) throws JsonProcessingException, BlockChainException {
        if (blocks.isEmpty()){
            String expectedHash = Block.calculateHash(block.getData(),tail,hashEncoder);
            if (expectedHash.equals(block.getHash())) blocks.add(block);
            else throw new BlockChainException("invalid hash");
            return;
        }


        String tail =  blocks.getLast().getHash();


        if (block.getParentHash().equals(tail)){

            String expectedHash = Block.calculateHash(block.getData(),tail,hashEncoder);
            if (expectedHash.equals(block.getHash())) blocks.add(block);
            else {
                throw new BlockChainException("Invalid hash");
            }

        }
        else {
            throw new BlockChainException("Invalid parent hash");}
    }
    public ArrayList<Block<T>> getBlocks(){
        return blocks;
    }

    public String getTail() {
        return tail;
    }

    public int getBlockNumber() {
        return blockNumber;
    }


}
