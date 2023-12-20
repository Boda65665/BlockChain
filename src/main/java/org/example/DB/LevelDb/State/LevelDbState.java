package org.example.DB.LevelDb.State;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.example.Entity.Address;
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
            String path = basePath+"\\src\\main\\java\\org\\example\\DB\\LevelDb\\State\\bd";
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
//        levelDbState.getAll();
//        String states = "[{\"publicKey\":\"ddd\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]},{\"publicKey\":\"s\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]},{\"publicKey\":\"shh\",\"balance\":0,\"nonce\":0,\"transactionsComplete\":[]}]";
//        levelDbState.buildStates(states);

        Address address = new Address("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEU7pBO372GCyyq0AahmTwJbkCOfc+O7Y+tBFtqRhSEpPdMpXdPDKzkBWNLsIU7waK",110,null,0);
        levelDbState.put(address);

        System.out.println(levelDbState.getAll().size());
    }

}
