package org.example.CustomBlockChain.DB.LevelDB.TransactonPool;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.CustomBlockChain.Entity.Transaction;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class LevelDbTransactionPool {
    private final Gson gson = new Gson();

    static public DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\CustomBlockChain\\DB\\LevelDb\\TransactonPool\\bd";
            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Transaction transaction) throws IOException {
        DB db = connectDb();
        String key = new HashEncoder().SHA256(transaction.getHash());
        String value = gson.toJson(transaction);
        db.put(bytes(key),bytes(value));
        db.close();
    }

    public Transaction get(String key) throws IOException {
        DB db = connectDb();
        String transactionJson = asString(db.get(bytes(key)));
        Transaction transaction = gson.fromJson(transactionJson,Transaction.class);
        db.close();
        return  transaction;
    }
    public void update(Transaction transaction) throws IOException {
        put(transaction);
    }
    public ArrayList<Transaction> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();
        iterator.seekToFirst();
        ArrayList<Transaction> transactions = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> entry = iterator.next();
            String value = asString(entry.getValue());
            Transaction transaction = gson.fromJson(value,Transaction.class);
            transactions.add(transaction);
        }
        db.close();
        return transactions;
    }
    public void buildTransactionPool(String transactionJson) throws IOException {
        DB db = connectDb();
        Type statesType = new TypeToken<ArrayList<Transaction>>(){}.getType();
        ArrayList<Transaction> transactions = gson.fromJson(transactionJson,statesType);
        for (Transaction transaction : transactions) {
            String key = new HashEncoder().SHA256(transaction.getHash());
            db.put(bytes(key),bytes(gson.toJson(transaction)));
        }
        db.close();
    }
    public void clear() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();
        iterator.seekToFirst();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> entry = iterator.next();
            String key = asString(entry.getKey());
            db.delete(bytes(key));
        }
        db.close();
    }


}
