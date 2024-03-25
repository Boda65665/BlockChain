package org.example;

import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;
import org.example.BlockChainBase.BlockChain.BlockChain;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.Entity.Block;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.TypeUpdateRequestNodeCommunication;
import org.example.CustomBlockChain.Servise.Validation.ValidNodeCommunicationServise;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ValidNodeCommunicationServiseTest {
    HashEncoder hashEncoder = new HashEncoder();
    BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(hashEncoder);
    JavaChain javaChain = new JavaChain(blockChain);
    ValidNodeCommunicationServise validNodeCommunicationServise = new ValidNodeCommunicationServise(javaChain);

    public ValidNodeCommunicationServiseTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void updateRequest() throws Exception {
        NodeCommunicationServer.IsUpdateRequest isUpdateRequest = NodeCommunicationServer.IsUpdateRequest.newBuilder().setLastTransactionHash("141ae8f325650ac64a349b68dfa340767ac387c8b13ccf5418d1998e7f444816").build();
        Assertions.assertNotNull(validNodeCommunicationServise.validIsUpdateStatus(isUpdateRequest));

    }
}
