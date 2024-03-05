package org.example.BlockChainBase.DB.LevelDb.Block;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.checkerframework.checker.units.qual.A;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.Block.Keys.LevelDbKeysBlockStruct;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.BlockChainBase.Entity.Block;

import org.example.CustomBlockChain.Entity.Transaction;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class LevelDbBlock<T>{

    private final Gson gson = new Gson();
    private final LevelDbKeysBlockStruct levelDbKeysBlockStruct = new LevelDbKeysBlockStruct();
    private final Type typeData;
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    public LevelDbBlock(Type typeData) {
        this.typeData = typeData;
    }

    private DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\Block\\bd";
            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Block<T> block) throws IOException {
        DB db = connectDb();
        String key = String.format("block%d_%s",block.getBlockNumber(),block.getHash());
        levelDbKeysBlockStruct.put(block.getHash(),block.getBlockNumber());
        String value = gson.toJson(block);
        db.put(bytes(key),bytes(value));
        db.close();
    }
    public Block<T> getByHash(String key) throws IOException {
        DB db = connectDb();
        key = levelDbKeysBlockStruct.getByHash(key);
        if (key==null) {
            db.close();
            return null;

        }
        String blockJson = asString(db.get(bytes(key)));
        Type blockType = new TypeToken<Block<T>>(){}.getType();
        Block<T> block = gson.fromJson(blockJson, blockType);

        block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
        db.close();
        return block;
    }
    public Block<T> getByNumber(long number) throws IOException {
        DB db = connectDb();
        String key = levelDbKeysBlockStruct.getByNumber(number);
        if (key==null) {
            db.close();
            return null;

        }
        String blockJson = asString(db.get(bytes(key)));
        Type blockType = new TypeToken<Block<T>>(){}.getType();
        Block<T> block = gson.fromJson(blockJson, blockType);

        block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
        db.close();
        return block;
    }
    public ArrayList<Block<T>> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();

        iterator.seekToFirst();
        ArrayList<Block<T>> blocks = new ArrayList<>();
        Type blockType = new TypeToken<Block<T>>(){}.getType();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> entry = iterator.next();
            String value = asString(entry.getValue());
            String key = asString(entry.getKey());
            Block<T> block = gson.fromJson(value, blockType);
            block.setData(gson.fromJson(gson.toJson(block.getData()),typeData));
            blocks.add(block);

        }
        db.close();
        return blocks;
    }

    public static void main(String[] args) throws IOException {
        Type typeData = new TypeToken<ArrayList<Transaction>>(){}.getType();
        LevelDbBlock<ArrayList<Transaction>> levelDbBlock = new LevelDbBlock<>(typeData);
        System.out.println(levelDbBlock.getAll());
    }







    }





