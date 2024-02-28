package org.example.BlockChainBase.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.DB.LevelDb.Block.LevelDbBlock;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.LevelDbPoolBlock;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.BlockChainBase.DB.SQL.Node.NodeListDB;
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
    private final LevelDbState levelDbState = new LevelDbState();
    NodeListDB nodeListDB = new NodeListDB();
    String tail = null;
    int blockNumber = 0;
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
        addresses = levelDbState.getAll();
        if (!blocks.isEmpty()) tail = blocks.get(blocks.size()-1).getHash();
    }
    public void addBlock(Block<T> block) throws Exception {
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
             //   blockChainInfoBD.addInfo(block.getHash(),blockChainInfoBD.incHeight());
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



    @Override
    public boolean isValid(ArrayList<Block<T>> blocks) throws JsonProcessingException, BlockChainException {
        if (blocks.isEmpty()) return false;

        ArrayList<Block<T>> constructBlockchain = new ArrayList<>();

        for (int i = 0;i<blocks.size();i++) {
            Block<T> block = blocks.get(i);


            constructBlockchain.add(block);
            if (!poWRule.Execute(constructBlockchain,block)) return false;
            System.out.println(block);
            if (block.getParentHash()==null && block.getBlockNumber()==1){
                String expectedHash = Block.calculateHash(block.getData(),null,hashEncoder,block.getNonce());
                if (!expectedHash.equals(block.getHash())){
                    return false;
                }
            }
            else {
                String expectedHash = Block.calculateHash(block.getData(),block.getParentHash(),hashEncoder,block.getNonce());

                if (!block.getParentHash().equals(blocks.get(i-1).getHash()) || !expectedHash.equals(block.getHash())) {

                    return false;
                }

            }

        }

        return true;
    }

    @Override
    public boolean isQueryValid() {
        int height = getBlocks().size();
        String lastHash = blocks.getLast().getHash();
        return true;
    }

    @Override
    public void scanBlockChain() throws Exception {
        ArrayList<String> ipsNodes = nodeListDB.getAllIp();
        for (String ipsNode : ipsNodes) {

        }
    }

    @Override
    public void addAll(ArrayList<Block<T>> blocks) throws Exception {
        for (Block<T> block : blocks) {
            addBlock(block);
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
