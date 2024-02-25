package org.example.CustomBlockChain.API.GRPC;


import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import node.api.grc.NodeAPIServiseGrpc;
import node.api.grc.NodeApi.*;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;

public class Client {
    public static void main(String[] args) throws SQLException, IOException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException, ClassNotFoundException {
        JavaChainMethodService javaChainMethodService = new JavaChainMethodService();
        ManagedChannel managedChannel = ManagedChannelBuilder.forTarget("localhost:8080").usePlaintext().build();

        NodeAPIServiseGrpc.NodeAPIServiseBlockingStub stub = NodeAPIServiseGrpc.newBlockingStub(managedChannel);
        GetInfoBlockChainRequest request = GetInfoBlockChainRequest.newBuilder().build();
        System.out.println(stub.getBlockChainInfo(request));
        managedChannel.shutdown();
    }

}
