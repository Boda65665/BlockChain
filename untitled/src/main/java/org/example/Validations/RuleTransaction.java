package org.example.Validations;

import jdk.jshell.Snippet;
import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Entity.TransactionStatus;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public class RuleTransaction implements RuleBase<ArrayList<Transaction>> {
    Asymmetric asymmetric = new Asymmetric();
    HashEncoder hashEncoder = new HashEncoder();



    @Override
    public void Execute(ArrayList<Block<ArrayList<Transaction>>> blocks,Block<ArrayList<Transaction>> newBlock) {
        for (Transaction transaction : newBlock.getData()) {
            transaction.setBlockNumber(blocks.size());
            //Check signature correct and balance(transaction.value<=balance)
            if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo().getPublicKey()), transaction.getFrom().getPublicKey(),transaction.getSing()) || transaction.getValue()>transaction.getFrom().getBalance()){
                transaction.setStatus(TransactionStatus.UNSUCCESSFULLY);
            }
            else {
                transaction.setStatus(TransactionStatus.SUCCESSFULLY);
            }


        }



    }



}

