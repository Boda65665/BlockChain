package org.example;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import node.communication.base.NodeCommunicationServer;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.NodeCommunication.NodeClient;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class NodeClientTest {

    JavaChainMethodService javaChainMethodService = new JavaChainMethodService();
    JavaChain javaChain = javaChainMethodService.getJavaChain();
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    NodeClient nodeClient = new NodeClient(javaChain);

    public NodeClientTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void isUpdateBlock() throws IOException {

//        Assertions.assertTrue(nodeClient.update(javaChain.getBlocks().getLast()));
        Assertions.assertTrue(nodeClient.update(levelDbTransaction.getAll().getLast()));
    }
    @Test
    void Update(){

    }
}
