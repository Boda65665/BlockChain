package org.example.BlockChainBase.DB.LevelDb.State;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.BlockChainBase.Entity.Address;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

import static org.iq80.leveldb.impl.Iq80DBFactory.*;

public class LevelDbState {

    private final Gson gson = new Gson();

    static public DB connectDb(){
        Options options = new Options();
        options.createIfMissing(true);
        try {
            String basePath = System.getProperty("user.dir");
            String path = basePath+"\\src\\main\\java\\org\\example\\BlockChainBase\\DB\\LevelDb\\State\\bd";
            return factory.open(new File(path), options);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    public void put(Address address) throws IOException {

        DB db = connectDb();
        String key = address.getPublicKey();
        String value = gson.toJson(address);
        db.put(bytes(key),bytes(value));
        db.close();
    }
    public void put(String value,String key) throws IOException {
        DB db = connectDb();
        db.put(bytes(key),bytes(value));
        db.close();
    }
    public void update(String value,String key) throws IOException {
        put(value,key);

    }
    public Address get(String key) throws IOException {
        DB db = connectDb();
        String addressJson = asString(db.get(bytes(key)));
        Address address = gson.fromJson(addressJson,Address.class);
        db.close();
        return  address;
    }

    public void update(Address address) throws IOException {
        put(address);
    }
    public ArrayList<Address> getAll() throws IOException {
        DB db = connectDb();
        DBIterator iterator = db.iterator();
        iterator.seekToFirst();
        ArrayList<Address> addresses = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<byte[], byte[]> entry = iterator.next();
            String value = asString(entry.getValue());
            Address address = gson.fromJson(value,Address.class);
            addresses.add(address);
        }
        db.close();
        return addresses;
        }
        public void buildStates(String statesJson) throws IOException {
            DB db = connectDb();
            Type statesType = new TypeToken<ArrayList<Address>>(){}.getType();
            ArrayList<Address> addresses = gson.fromJson(statesJson,statesType);
            for (Address address : addresses) {
                db.put(bytes(address.getPublicKey()),bytes(gson.toJson(address)));
            }
            db.close();
        }



    public static void main(String[] args) throws IOException {
        LevelDbState levelDbState = new LevelDbState();
        for (Address address : levelDbState.getAll()) {
            System.out.println(address);
        }
//        String states = "[{\"publicKey\":\"ddd\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]},{\"publicKey\":\"s\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]},{\"publicKey\":\"shh\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]}]";
//        levelDbState.buildStates(states);
//
        AddressCustom address = new AddressCustom("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu",4321,new ArrayList<>(),0,0);
        levelDbState.update(address);
//        System.out.println(levelDbState.get("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu").getBalance());
//        AddressCustom addressCustom = (AddressCustom)levelDbState.get("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu",AddressCustom.class);
//        System.out.println(addressCustom.getTransactionsComplete());
//        AddressCustom fromAddressCustom = (AddressCustom) levelDbState.get("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu",AddressCustom.class);
//        System.out.println(fromAddressCustom.getTransactionsComplete());

    }

}
