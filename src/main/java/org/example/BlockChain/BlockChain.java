package org.example.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.reflect.TypeToken;
import org.example.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.Cryptography.HashEncoder;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.DB.LevelDb.State.LevelDbState;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BlockChain<T> implements BlockChainBase<T>{

    private final HashEncoder hashEncoder;
    private final ArrayList<Address> addresses;
    private final LevelDbState levelDbState = new LevelDbState();
    String tail = null;
    int blockNumber = 0;
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    private final ArrayDeque<Block<T>> blocksPool;
    private final LevelDbBlock<T> levelDbBlock = new LevelDbBlock<>(typeData);
    private final LevelDbPoolBlock<T> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    private final ArrayList<Block<T>> blocks;

    public BlockChain(HashEncoder hashEncoder) throws IOException {
        this.hashEncoder = hashEncoder;
        blocks=levelDbBlock.getAll();
        blocksPool = levelDbPoolBlock.getAll();
        addresses = levelDbState.getAll();
        if (!blocks.isEmpty()) tail = blocks.get(blocks.size()-1).getHash();
    }
    public void addBlock(Block<T> block) throws JsonProcessingException, BlockChainException {

        if (blocks.isEmpty()){
            String expectedHash = Block.calculateHash(block.getData(),tail,hashEncoder,block.getNonce());
            if (expectedHash.equals(block.getHash())) blocks.add(block);
            else throw new BlockChainException("invalid hash");
            tail=block.getHash();
            return;
        }




        if (block.getParentHash().equals(tail)){

            String expectedHash = Block.calculateHash(block.getData(),tail,hashEncoder,block.getNonce());
            if (expectedHash.equals(block.getHash())) {
                blocks.add(block);
                tail=block.getHash();
            }
            else {
                throw new BlockChainException("Invalid hash");
            }

        }
        else {
            throw new BlockChainException("Invalid parent hash");}
    }
    public void addBlockToPoll(Block<T> block) throws IOException {
        blocksPool.add(block);
        levelDbPoolBlock.put(block);
    }
    public Block<T> popBlockFromPoll() throws IOException {
        if (blocksPool.peek()==null) return null;
        levelDbPoolBlock.deleteByHash(blocksPool.peek().getHash());
        return blocksPool.pop();
    }
    public ArrayList<Block<T>> getBlocks(){
        return blocks;
    }

    @Override
    public ArrayList<Block<T>> getBlocksStartingFrom(String hash) {
        ArrayList<Block<T>> foundBlocks = new ArrayList<>();
        boolean isFound = false;
        for (Block<T> block : blocks) {
            if (hash.equals(block.getHash())) isFound=true;
            if (isFound) foundBlocks.add(block);
        }
        return foundBlocks;
    }

    @Override
    public ArrayList<Address> getAddressStartingFrom(String publicKey) {
        ArrayList<Address> foundAddress = new ArrayList<>();
        boolean isFound = false;
        for (Address address : addresses) {
            if (publicKey.equals(address.getPublicKey())) isFound=true;
            if (isFound) foundAddress.add(address);
        }
        return foundAddress;
    }

    @Override
    public ArrayDeque<Block<T>> getBlocksPool() {
        return blocksPool;
    }

    @Override
    public ArrayList<Address> getAllAddresses() {
        return addresses;
    }

    public String getTail() {
        return tail;
    }

    public int getBlockNumber() {
        return blockNumber;
    }


}
