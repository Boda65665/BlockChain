package org.example.CustomBlockChain.DB.LevelDB.Transaction;

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

public class LevelDbTransaction {
    private final Gson gson = new Gson();

    static public DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\Transaction\\bd";
            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Transaction transaction) throws IOException {
        DB db = connectDb();
        String key = transaction.getHash();
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
        Type transactionType = new TypeToken<ArrayList<Transaction>>(){}.getType();
        ArrayList<Transaction> transactions = gson.fromJson(transactionJson,transactionType);
        for (Transaction transaction : transactions) {
            String key = new HashEncoder().SHA256(transaction.getHash());
            db.put(bytes(key),bytes(gson.toJson(transaction)));
        }
        db.close();
    }

    public static void main(String[] args) throws IOException {
        LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
        for (Transaction transaction1 : levelDbTransaction.getAll()) {
            System.out.println(transaction1.getHash());
        }
    }
}
