package org.example.Rules;

import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
import org.example.DB.LevelDb.State.LevelDbState;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class TransactionRule implements RuleBase<Transaction>{
    LevelDbState levelDbState = new LevelDbState();
    Asymmetric asymmetric = new Asymmetric();
    HashEncoder hashEncoder = new HashEncoder();
    Logger logger = Logger.getLogger("transactionRule");







    @Override
    public void Execute(ArrayList<Block<Transaction>> blocks, Block<Transaction> newBlock) throws BlockChainException, IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        Transaction transaction = newBlock.getData();
        Address fromAddress = levelDbState.get(transaction.getFrom().getPublicKey());
        if (fromAddress==null){
            throw new BlockChainException("invalid wallet address");
        }
        if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo().getPublicKey()+transaction.getNonce()),transaction.getFrom().getPublicKey(),transaction.getSing())){
            throw new BlockChainException("Invalid signature");
        }
        if (transaction.getValue()>fromAddress.getBalance() || transaction.getValue()<0){
            throw new BlockChainException("not enough money");
        }
//        if(transaction.getNonce() < fromAddress.getNonce()){
//            throw new BlockChainException("invalid nonce");
//        }
    }
    public boolean Execute(Transaction transaction){
        try{
            Execute(null,new Block<>(transaction));
            return true;

        } catch (BlockChainException | SignatureException | NoSuchAlgorithmException | InvalidKeyException |
                 NoSuchProviderException | IOException e) {
            LogRecord log = new LogRecord(Level.WARNING,e.toString());
            logger.log(log);
            return false;
        }
    }
}

