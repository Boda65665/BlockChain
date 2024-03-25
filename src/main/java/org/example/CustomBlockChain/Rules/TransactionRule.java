package org.example.CustomBlockChain.Rules;

import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.DB.LevelDb.State.LevelDbState;
import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Rules.RuleBase;
import org.example.CustomBlockChain.Entity.Transaction;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TransactionRule implements RuleBase<Transaction> {
    LevelDbState levelDbState = new LevelDbState();
    Asymmetric asymmetric = new Asymmetric();
    HashEncoder hashEncoder = new HashEncoder();

    Logger logger = Logger.getLogger("transactionRule");






    @Override
    public boolean Execute(ArrayList<Block<Transaction>> blocks, Block<Transaction> newBlock) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        Transaction transaction = newBlock.getData();
        Address fromAddress = levelDbState.get(transaction.getFrom());

        if (fromAddress==null) return false;

        if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo()+transaction.getNonce()),transaction.getFrom(),transaction.getSing())) return false;
        if (transaction.getValue()>fromAddress.getBalance() || transaction.getValue()<0) return false;
        if(transaction.getNonce() < fromAddress.getNonce()) return false;
        return true;
    }
    public boolean validTransactions(ArrayList<Transaction> transactions) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        for (Transaction transaction : transactions) {
            Address fromAddress = levelDbState.get(transaction.getFrom());
            if (fromAddress==null) return false;
            if (!hashEncoder.SHA256(transaction.getFrom()+transaction.getValue()+transaction.getNonce()).equals(transaction.getHash())) return false;

            if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo() + transaction.getNonce()), transaction.getFrom(), transaction.getSing())) return false;

            if (transaction.getValue()>fromAddress.getBalance() || transaction.getValue()<0) return false;

        }
        //sss
        return true;
    }
    public boolean Execute(Transaction transaction) {
        try {
            return Execute(null, new Block<>(transaction));
        } catch (IOException | SignatureException | NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchProviderException e) {

            System.out.println(e.toString());
        }
            return false;
        }

}

