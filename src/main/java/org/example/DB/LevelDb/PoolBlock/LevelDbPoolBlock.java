package org.example.DB.LevelDb.PoolBlock;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.DB.LevelDb.Block.LevelDbBlock;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
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

    public LevelDbPoolBlock(Type typeData) {
        this.typeData = typeData;
    }

    private DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\DB\\LevelDb\\PoolBlock\\bd";
            return factory.open(new File(path), options);
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
        String blockJson = asString(db.get(bytes(key)));
        Type blockType = new TypeToken<Block<T>>(){}.getType();
        Block<T> block = gson.fromJson(blockJson, blockType);
        if (block==null) {
            db.close();
            return null;

        }
        block.setData(gson.fromJson(gson.toJson(block.getData()), typeData));
        db.close();
        return block;
    }
    public ArrayDeque<Block<T>> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();

        iterator.seekToFirst();
        ArrayDeque<Block<T>> blocks = new ArrayDeque<>();
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
    public void buildBlockChain(String blocksJson) throws IOException {
        DB db = connectDb();
        Type blocksType = new TypeToken<ArrayList<Block<T>>>(){}.getType();
        Type poolBlocksType = new TypeToken<ArrayDeque<Block<T>>>(){}.getType();
        ArrayList<Block<T>> blocks = gson.fromJson(blocksJson,blocksType);
        for (Block<T> block : blocks) {
            String key = block.getHash();
            String value = gson.toJson(block);
            db.put(bytes(key),bytes(value));
        }
        db.close();
    }
    public void deleteByHash(String hash) throws IOException {
        if (get(hash)!=null) return;
        DB db = connectDb();
        db.delete(bytes(hash));
        db.close();
    }



    public static void main(String[] args) throws IOException {
        Type type = new TypeToken<ArrayList<Transaction>>(){}.getType();
        LevelDbPoolBlock<ArrayList<Transaction>> LevelDbPoolBlock =new LevelDbPoolBlock<>(type);
        ArrayList<Transaction> transactions = new ArrayList<>();
        ArrayDeque<Block<ArrayList<Transaction>>> blocks = LevelDbPoolBlock.getAll();

        while (blocks.peek()!=null) System.out.println(blocks.pop().getData().get(0).getFrom().getPublicKey());




    }
}
