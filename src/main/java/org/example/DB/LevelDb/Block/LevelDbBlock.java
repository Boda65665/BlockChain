package org.example.DB.LevelDb.Block;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
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
    private final Type typeData;
    public LevelDbBlock(Type typeData) {
        this.typeData = typeData;
    }

    private DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\DB\\LevelDb\\Block\\bd";
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



        public static void main(String[] args) throws IOException {
            Type type = new TypeToken<ArrayList<Transaction>>(){}.getType();
            LevelDbBlock<ArrayList<Transaction>> levelDbBlock =new LevelDbBlock<>(type);
//            ArrayList<Transaction> transactions = new ArrayList<>();
//            Transaction transaction = new Transaction(new Address("fdewf"),0,0,"fed",null,0,"Fesf",0);
//            transactions.add(transaction);
//            transactions.add(transaction);
//
//
//            Block<ArrayList<Transaction>> block = new Block<>(transactions);
//            block.setHash("dwtdfsr");
//            levelDbBlock.put(block);
//            Gson gson1  = new Gson();
            System.out.println(levelDbBlock.getAll().get(0).getData().get(0).getFrom().getPublicKey());




    }




}
