package org.example.BlockChainBase.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.Entity.BlockType;
import org.example.CustomBlockChain.Entity.Transaction;

import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Exeptions.BlockChainException;
import org.example.BlockChainBase.Cryptography.HashEncoder;

import org.example.BlockChainBase.Rules.PoWRule;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public class BlockChain<T> implements BlockChainBase<T>{

    private final HashEncoder hashEncoder;
    private final ArrayList<Address> addresses;
    PoWRule<T> poWRule = new PoWRule<>();
    String tail = null;
    private final BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    private final Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
    private final ArrayDeque<Block<T>> blocksPool;
    private final LevelDbBlock<T> levelDbBlock = new LevelDbBlock<>(typeData);
    private final LevelDbPoolBlock<T> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
    private final ArrayList<Block<T>> blocks;

    public BlockChain(HashEncoder hashEncoder) throws IOException, SQLException, ClassNotFoundException {
        this.hashEncoder = hashEncoder;
        blocks=levelDbBlock.getAll();
        blocksPool = levelDbPoolBlock.getAll();
        LevelDbState levelDbState = new LevelDbState();
        addresses = levelDbState.getAll();
        if (!blocks.isEmpty()) tail = blocks.getLast().getHash();
    }
    public void addBlock(Block<T> block) throws BlockChainException, IOException, SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        if (isValid(block,tail)) {
            block.setBlockType(BlockType.CONFIRMED);
            levelDbBlock.put(block);
            blocks.add(block);
            blockChainInfoBD.addInfo(block.getHash(), getBlockNumber());
            tail=block.getHash();
        }
        else throw new BlockChainException("Invalid block");
    }
    public void addBlockToPoll(Block<T> block) throws IOException {
        blocksPool.add(block);
        levelDbPoolBlock.put(block);
    }



    @Override
    public boolean isValid(Block<T> block,String tail) throws JsonProcessingException {
        if (block==null) return false;
        if (block.getBlockType()==BlockType.CONFIRMED && !poWRule.Execute(blocks,block)) return false;

        if (block.getParentHash()==null && block.getBlockNumber()==1){
            if (tail!=null)return false;
            String expectedHash = Block.calculateHash(block.getData(),null,hashEncoder,block.getNonce());
            return expectedHash.equals(block.getHash());
        }
        if (!block.getParentHash().equals(tail))return false;
        String expectedHash = Block.calculateHash(block.getData(), block.getParentHash(), hashEncoder, block.getNonce());
        return  expectedHash.equals(block.getHash());
    }

    @Override
    public boolean isQueryValid(String lastHash, int height, BlockChainInfoBD.BlockChainInfoStruct actualInfoBlock) {
        return equalsHashBlock(lastHash,actualInfoBlock.lastHsh()) && actualInfoBlock.height() == height;
    }
    public boolean equalsHashBlock(String hashFirst,String hashSecond){
        if (hashFirst==null) return hashSecond==null;
        return (hashFirst.equals(hashSecond));

    }
    @Override
    public void scanBlockChain()  {

    }

    @Override
    public boolean isValidAllBlock(ArrayList<Block<T>> blocks) throws JsonProcessingException {
        for (Block<T> block : blocks) {
            if (!isValid(block,block.getParentHash())) return false;
        }
        return true;
    }

    @Override
    public void addAll(ArrayList<Block<T>> blocks) throws Exception {
        for (Block<T> block : blocks) {
            addBlock(block);
        }
    }

    @Override
    public void addAllToBlockPoll(ArrayList<Block<T>> blocks) throws Exception {
        for (Block<T> block : blocks) {
            addBlockToPoll(block);
        }
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
    public ArrayList<Block<T>> getBlocksStartingFrom(int numberBlock) {
        return new ArrayList<>(blocks.subList(numberBlock, blocks.size()));
    }
    @Override
    public ArrayDeque<Block<T>> getBlocksPool() {
        return blocksPool;
    }
    public int getBlockNumberFromBlockPool(){
        return blocksPool.size()+1;
    }
    @Override
    public String getTailFromBlockPoll(){
        if (blocksPool.isEmpty()) return null;
        return blocksPool.getLast().getHash();
    }

    @Override
    public ArrayList<Address> getAllAddresses() {
        return addresses;
    }

    public String getTail() {
        return tail;
    }

    public int getBlockNumber() {
        return blocks.size();
    }


    public boolean isAlreadyExistBlock(int blockNumber) throws SQLException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        return blockChainInfoBD.getBlockChainInfo().height()>=blockNumber;
    }

    @Override
    public boolean isAlreadyExistBlockPending(int blockNumber) throws Exception {
        return blocksPool.getLast().getBlockNumber()>=blockNumber;
    }
}
