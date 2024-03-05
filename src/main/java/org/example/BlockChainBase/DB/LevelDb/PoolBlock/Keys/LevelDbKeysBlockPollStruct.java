package org.example.BlockChainBase.DB.LevelDb.PoolBlock.Keys;

import com.google.gson.Gson;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class LevelDbKeysBlockPollStruct {



        private final Gson gson = new Gson();

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
        public void put(String key,int numberBlock) throws IOException {
            DB db = connectDb();
            String value = String.format("block%d_%s",numberBlock,key);
            db.put(bytes(key),bytes(value));
            db.close();
        }
        public String get(String key) throws IOException {
            DB db = connectDb();
            String value = asString(db.get(bytes(key)));
            db.close();
            return value;

        }
        public boolean deleteByHash(String key) throws IOException {
            if (get(key)==null)return false;
            DB db = connectDb();
            db.delete(bytes(key));
            db.close();
            return true;

        }

        public static void main(String[] args) throws IOException {
            LevelDbKeysBlockPollStruct levelDbKeysBlockStruct = new LevelDbKeysBlockPollStruct();
            System.out.println(levelDbKeysBlockStruct.get("c6df7dd32737c7ffdc3a96245df5715fb995a965e18e09d9298202da0a37d90a"));
        }
    }


