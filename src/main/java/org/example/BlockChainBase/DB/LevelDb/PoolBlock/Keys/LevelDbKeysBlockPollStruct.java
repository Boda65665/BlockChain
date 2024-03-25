package org.example.BlockChainBase.DB.LevelDb.PoolBlock.Keys;

import com.google.gson.Gson;
import org.example.CustomBlockChain.Servise.BlockKeyService;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class LevelDbKeysBlockPollStruct {



        private final Gson gson = new Gson();
        BlockKeyService keyService = new BlockKeyService();

        private DB connectDb(){
            Options options = new Options();
            options.createIfMissing(true);
            try {
                String basePath = System.getProperty("user.dir");
                String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\PoolBlock\\Keys\\bd";
                return factory.open(new File(path), options);
            } catch (IOException e) {
                throw new RuntimeException(e);

            }
        }
        public void put(String key,int numberLastBlock) throws IOException {
            DB db = connectDb();
            db.put(bytes(keyService.valueOf(numberLastBlock)),bytes(key));
            db.close();
        }
    public String getByNumber(long number) throws IOException {
        DB db = connectDb();
        String value = asString(db.get(bytes(keyService.valueOf(number))));
        db.close();
        return value;

    }

    public String getByHash(String key) throws IOException {
            DB db = connectDb();
            String value = asString(db.get(bytes(key)));
            db.close();
            return value;

        }
        public void deleteByNumber(int numberBlock) throws IOException {
            String key = keyService.valueOf(numberBlock);
            if (getByHash(key)==null)return;
            DB db = connectDb();
            db.delete(bytes(key));
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

        public void main(String[] args) throws IOException {
            LevelDbKeysBlockPollStruct levelDbKeysBlockStruct = new LevelDbKeysBlockPollStruct();
            System.out.println(levelDbKeysBlockStruct.getByHash("c6df7dd32737c7ffdc3a96245df5715fb995a965e18e09d9298202da0a37d90a"));
        }
    }


