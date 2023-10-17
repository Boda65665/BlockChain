package org.example.Rules;

import org.example.Cryptography.Asymmetric;
import org.example.Cryptography.HashEncoder;
import org.example.Entity.Block;
import org.example.Entity.Transaction;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public class TransactionRule implements RuleBase<Transaction>{
    Asymmetric asymmetric = new Asymmetric();
    HashEncoder hashEncoder = new HashEncoder();



    @Override
    public void Execute(ArrayList<Block<Transaction>> blocks, Block<Transaction> newBlock) throws BlockChainException {
        Transaction transaction = newBlock.getData();
        if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo().getPublicKey()),transaction.getTo().getPublicKey(),transaction.getSing())){
            throw new BlockChainException("Invalid signature");
        }
        if (transaction.getValue()>transaction.getFrom().getBalance()){
            throw new BlockChainException("not enough money");
        }

    }

    public ArrayList<Transaction> executeTransactions(ArrayList<Block<ArrayList<Transaction>>> blocks, Block<ArrayList<Transaction>> newBlock) throws BlockChainException {
        ArrayList<Transaction> validTransactions = new ArrayList<>();

        for (Transaction transaction : newBlock.getData()) {
                try {
                    Execute(null,new Block<Transaction>(transaction));
                    validTransactions.add(transaction);

                }
                catch (BlockChainException e){
                    e.printStackTrace();
                }
        }
        return validTransactions;
    }

}

