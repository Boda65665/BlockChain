package org.example.BlockChainBase.DB.LevelDb.PoolBlock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.DB.LevelDb.PoolBlock.Keys.LevelDbKeysBlockPollStruct;
import org.example.BlockChainBase.Entity.Block;

import org.example.CustomBlockChain.Entity.Transaction;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class LevelDbPoolBlock<T> {
    private final Gson gson = new Gson();
    private final Type typeData;
    private final LevelDbKeysBlockPollStruct levelDbKeysBlockPollStruct = new LevelDbKeysBlockPollStruct();

    public LevelDbPoolBlock(Type typeData) {
        this.typeData = typeData;
    }

    private DB connectDb() {
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath + "\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\PoolBlock\\bd";

            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Block<T> block) throws IOException {
        DB db = connectDb();
        String key = block.getHash();
        levelDbKeysBlockPollStruct.put(block.getHash());
        String value = gson.toJson(block);
        db.put(bytes(key), bytes(value));
        db.close();
    }

    public Block<T> getByHash(String key) throws IOException {
        DB db = connectDb();
        String value = asString(db.get(bytes(key)));
        if (value==null) {
            db.close();
            return null;
        }
        Type blockType = new TypeToken<Block<T>>() {
        }.getType();
        Block<T> block = gson.fromJson(value, blockType);
        block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
        db.close();
        return block;
    }

    public Block<T> getByNumber(long number) throws IOException {
        DB db = connectDb();
        String key = levelDbKeysBlockPollStruct.getByNumber(number);
        if (key == null) {
            db.close();
            return null;

        }
        String value = asString(db.get(bytes(key)));
        Type blockType = new TypeToken<Block<T>>() {
        }.getType();
        Block<T> block = gson.fromJson(value, blockType);
        block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
        db.close();
        return block;

    }

    public ArrayDeque<Block<T>> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();

        iterator.seekToFirst();
        ArrayList<String> hashes = levelDbKeysBlockPollStruct.getAll();
        ArrayDeque<Block<T>> blocks = new ArrayDeque<>();
        Type blockType = new TypeToken<Block<T>>() {
        }.getType();
        for (String hash : hashes) {
            String value = asString(db.get(bytes(hash)));
            Block<T> block = gson.fromJson(value, blockType);
            block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
            blocks.add(block);
        }
        db.close();
        return blocks;
    }


    public void deleteByHash(String hash) throws IOException {
        if (getByHash(hash) == null) return;
        levelDbKeysBlockPollStruct.deleteByNumber(getByHash(hash).getBlockNumber());
        DB db = connectDb();
        db.delete(bytes(hash));
        db.close();
        }

    public static void main(String[] args) throws IOException {
            Type typeData = new TypeToken<ArrayList<Transaction>>() {
            }.getType();
            LevelDbPoolBlock<ArrayList<Transaction>> levelDbPoolBlock = new LevelDbPoolBlock<>(typeData);
        for (Block<ArrayList<Transaction>> block : levelDbPoolBlock.getAll()) {
            System.out.println(block.getBlockNumber());
        }


        }

    }

