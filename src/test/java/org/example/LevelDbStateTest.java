package org.example;

import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.Entity.Address;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;


public class LevelDbStateTest {
    LevelDbState levelDbState = new LevelDbState();
    @Test
    void add() throws IOException {
        Address address = new Address("MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu",7777,0,0);
        levelDbState.put(address);
        Assertions.assertNotNull(levelDbState.get(address.getPublicKey()));
    }
}

