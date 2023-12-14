//package org.example.Rules;
//
//import org.example.Cryptography.Asymmetric;
//import org.example.Cryptography.HashEncoder;
//import org.example.Entity.Block;
//import org.example.Entity.Transaction;
//import org.example.Exeptions.BlockChainException;
//
//import java.util.ArrayList;
//import java.util.logging.Level;
//import java.util.logging.LogRecord;
//import java.util.logging.Logger;
//
//public class TransactionRule implements RuleBase<Transaction>{
//    Asymmetric asymmetric = new Asymmetric();
//    HashEncoder hashEncoder = new HashEncoder();
//    Logger logger = Logger.getLogger("transactionRule");
//
//
//
//
//
//
//
//    @Override
//    public void Execute(ArrayList<Block<Transaction>> blocks, Block<Transaction> newBlock) throws BlockChainException {
//        Transaction transaction = newBlock.getData();
//        System.out.println(transaction.getSing());
//        if (!asymmetric.verify(hashEncoder.SHA256(transaction.getTo().getPublicKey()+transaction.getNonce()),transaction.getFrom().getPublicKey(),transaction.getSing())){
//            throw new BlockChainException("Invalid signature");
//        }
//        if (transaction.getValue()>transaction.getFrom().getBalance() || transaction.getValue()<0){
//            throw new BlockChainException("not enough money");
//        }
//        if(transaction.getNonce() <= transaction.getFrom().getNonce()){
//            throw new BlockChainException("invalid nonce");
//        }
//    }
//    public boolean Execute(Transaction transaction){
//        try{
//            Execute(null,new Block<>(transaction));
//            return true;
//
//        } catch (BlockChainException e) {
//            LogRecord log = new LogRecord(Level.WARNING,e.toString());
//            logger.log(log);
//            return false;
//        }
//    }
//}
//
