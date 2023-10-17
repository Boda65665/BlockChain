package org.example.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.Entity.Block;
import org.example.Cryptography.HashEncoder;

public class BuildBlockChainService<T> {
    private String tail = null;
    private final HashEncoder hashEncoder;
    int blockNumber;

    public BuildBlockChainService(){
        this.tail=null;
        this.hashEncoder=new HashEncoder();
        this.blockNumber = 0;
    }

    public Block<T> buildBlock(T objData) throws JsonProcessingException {

        String data = Block.calculateHash(objData,tail,hashEncoder);
        return new Block<T>(tail,data,blockNumber,objData);
    }
    public void acceptBlock(Block<T> block){
        tail=block.getHash();
    }




}
