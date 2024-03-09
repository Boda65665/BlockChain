package org.example.BlockChainBase.DB.LevelDb.Block.Keys;

import com.google.gson.Gson;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.Servise.BlockKeyService;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class LevelDbKeysBlockStruct {
    private final BlockKeyService keyService = new BlockKeyService();
    private final Gson gson = new Gson();

    private DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\Block\\Keys\\bd";


            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
    public void put(String key) throws IOException {
        DB db = connectDb();

        String value = String.format(key);
        db.put(bytes(String.valueOf(keyService.nextKey())),bytes(value));
        db.close();
    }
    public ArrayList<String> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();

        iterator.seekToFirst();
        ArrayList<String> hashes = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> entry = iterator.next();
            String value = asString(entry.getValue());
            hashes.add(value);
        }
        db.close();
        return hashes;
    }
    public String getByNumber(long number) throws IOException {
        DB db = connectDb();
        String value = asString(db.get(bytes(keyService.valueOf(number))));
        db.close();
        return value;

    }

    public static void main(String[] args) throws IOException {
        LevelDbKeysBlockStruct levelDbKeysBlockStruct = new LevelDbKeysBlockStruct();
        levelDbKeysBlockStruct.getAll();
    }
}
