package org.example;

import node.entity.Entity;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.CustomBlockChain.DB.LevelDB.Transaction.LevelDbTransaction;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Rules.TransactionRule;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.logging.Level;

public class AsymetricTest {
    Asymmetric asymmetric = new Asymmetric();
    LevelDbTransaction levelDbTransaction = new LevelDbTransaction();
    JavaChainMethodService javaChainMethodService = new JavaChainMethodService();
    HashEncoder hashEncoder = new HashEncoder();
    TransactionRule transactionRule= new TransactionRule();

    public AsymetricTest() throws SQLException, IOException, ClassNotFoundException {
    }

    @Test
    void isVerified() throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, NoSuchProviderException {
//        String sign = asymmetric.sign("","MHsCAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQEEYTBfAgEBBBgi268/zjbSgJnBaIEh68MbGc9naDSAmt6gCgYIKoZIzj0DAQGhNAMyAARviePfORnFBqOMldHupH2/62+W77omyacMHxAZgG1N/ninDsKpMfEm5Z8tVg84Fu4=");
//        System.out.println(asymmetric.verify("","MEkwEwYHKoZIzj0CAQYIKoZIzj0DAQEDMgAEb4nj3zkZxQajjJXR7qR9v+tvlu+6JsmnDB8QGYBtTf54pw7CqTHxJuWfLVYPOBbu",sign));
        Transaction transaction = levelDbTransaction.getAll().getLast();
        Assertions.assertTrue(asymmetric.verify(hashEncoder.SHA256(transaction.getTo() + (transaction.getNonce())),transaction.getFrom(),transaction.getSing()));
    }
}
