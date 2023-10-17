package org.example.BlockChainApps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.BlockChain.BlockChain;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;
import org.example.JavaChain;
import org.example.Service.BuildBlockChainService;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;

/**
 * Hello world!
 *
 */
public class App {
    static private final HashEncoder hashEncoder = new HashEncoder();
    static private BuildBlockChainService<ArrayList<Transaction>> buildService = new BuildBlockChainService<>();
    static Asymmetric asymmetric = new Asymmetric();
    static BlockChain<ArrayList<Transaction>> blockChain = new BlockChain<>(hashEncoder);
    static JavaChain javaChain = new JavaChain(blockChain,buildService);


    public static void main(String[] args) throws JsonProcessingException, BlockChainException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException {
        Address from = new Address(asymmetric.generateKeys());
        Address to = new Address(asymmetric.generateKeys());

        int gas = 10;
        int gasPrice = 100;

        ArrayList<Transaction> transactions = new ArrayList<>();
        for (int i = 0;i<20;i++){
            transactions.add(new Transaction(from,gas,gasPrice,asymmetric.sign(hashEncoder.SHA256(String.valueOf(i)+to.getPublicKey()), from.getPrivateKey()),i,to,i,10,null));
        }
        javaChain.addBlock(transactions);






    }
}
