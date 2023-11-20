package org.example.LevelDb.Block;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.LevelDb.CustomerDB;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.channels.OverlappingFileLockException;
import java.util.*;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class LevelDbBlock<T>{

    private final Gson gson = new Gson();

    private DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");

            return factory.open(new File(basePath+"\\src/main/java/org/example/LevelDb/Block/bd"), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Block<T> block) throws IOException {
        DB db = connectDb();
        String key = block.getHash();
        String value = gson.toJson(block);
        db.put(bytes(key),bytes(value));
        db.close();
    }
    public Block<T> get(String key) throws IOException {
        DB db = connectDb();
        Type typeData = new TypeToken<T>(){}.getType();

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
            if (!key.equals("pool_blocks")) {
                blocks.add(gson.fromJson(value, blockType));
            }
        }
        db.close();
        return blocks;
    }
    public void addToPoolBlocks(Block<T> block) throws IOException {
        Type type = new TypeToken<T>(){}.getType();
        ArrayDeque<Block<T>> pollBlocksDeque = getBlockFromPoolBlocks();
        pollBlocksDeque.add(block);
        DB db = connectDb();
        db.put(bytes("pool_blocks"),bytes(gson.toJson(pollBlocksDeque)));
        db.close();
    }



    public ArrayDeque<Block<T>> getBlockFromPoolBlocks() throws IOException {
        DB db = connectDb();
        try {
            Type typeData = new TypeToken<T>(){}.getType();

            Type blockType = new TypeToken<ArrayDeque<Block<T>>>(){}.getType();
            String blockJson = asString(db.get(bytes("pool_blocks")));


            if (blockJson == null) {
                db.close();
                return new ArrayDeque<Block<T>>();
            }

            ArrayDeque<Block<T>> blocks = gson.fromJson(blockJson, blockType);
            for (Block<T> block : blocks) {
                block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
            }

            db.close();
            return blocks;
        } catch (NullPointerException err) {
            db.close();
            return new ArrayDeque<Block<T>>();
        }

    }
    public void popBlockFromPoolBlocks() throws IOException {

        ArrayDeque<Block<T>> blocks = getBlockFromPoolBlocks();
        if (!blocks.isEmpty()) {
            blocks.pop();
            DB db = connectDb();
            db.put(bytes("pool_blocks"), bytes(gson.toJson(blocks)));
            db.close();
        }
    }


    public void buildBlockChain(String blocksJson,String pollBlocksJson) throws IOException {
        DB db = connectDb();
        Type blocksType = new TypeToken<ArrayList<Block<T>>>(){}.getType();
        Type poolBlocksType = new TypeToken<ArrayDeque<Block<T>>>(){}.getType();
        ArrayList<Block<T>> blocks = gson.fromJson(blocksJson,blocksType);
        ArrayDeque<Block<T>> poolBlocks = gson.fromJson(pollBlocksJson,poolBlocksType);
        for (Block<T> block : blocks) {
            String key = block.getHash();
            String value = gson.toJson(block);
            db.put(bytes(key),bytes(value));
        }
        String value = gson.toJson(poolBlocks);
        db.put(bytes("pool_blocks"),bytes(value));
        db.close();
    }

//fweffewfefefwefewf

        public static void main(String[] args) throws IOException {

        LevelDbBlock<String> levelDbBlock = new LevelDbBlock<>();
        Block<String> block1 = new Block<>("a");
        block1.setHash("a");
        levelDbBlock.put(block1);


    }




}
