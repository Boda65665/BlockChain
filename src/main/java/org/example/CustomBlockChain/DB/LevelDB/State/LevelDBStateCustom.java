package org.example.CustomBlockChain.DB.LevelDB.State;

import com.google.gson.Gson;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.Entity.Address;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.iq80.leveldb.DB;

import java.io.IOException;

import static org.iq80.leveldb.impl.Iq80DBFactory.asString;
import static org.iq80.leveldb.impl.Iq80DBFactory.bytes;

public class LevelDBStateCustom extends LevelDbState {
    Gson gson = new Gson();
    @Override
    public AddressCustom get(String key) throws IOException {
        DB db = connectDb();
        String addressJson = asString(db.get(bytes(key)));
        AddressCustom address = gson.fromJson(addressJson,AddressCustom.class);
        db.close();
        return  address;
    }
}
