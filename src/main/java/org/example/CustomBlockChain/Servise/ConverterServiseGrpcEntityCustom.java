package org.example.CustomBlockChain.Servise;

import node.entity.Entity;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Entity.BlockType;
import org.example.BlockChainBase.Service.ConverterServiseGrpcEntityBase;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConverterServiseGrpcEntityCustom implements ConverterServiseGrpcEntityBase<Entity.Transaction,Transaction, Entity.Address,AddressCustom,
        Entity.Block,Block<ArrayList<Transaction>>> {
    public ConverterServiseGrpcEntityCustom() throws SQLException, IOException, ClassNotFoundException {
    }


    @Override
    public Entity.Transaction dataBlockToGrpcData(Transaction transaction){
        return Entity.Transaction.newBuilder()
                .setTo(transaction.getTo())
                .setFrom(transaction.getFrom())
                .setBlockNumber(transaction.getBlockNumber())
                .setData(transaction.getData())
                .setGasPrice(transaction.getGasPrice())
                .setGas(transaction.getGas())
                .setStatus(transaction.isStatus())
                .setSing(transaction.getSing())
                .setHash(transaction.getHash())
                .setValue(transaction.getValue())
                .setNonce(transaction.getNonce())
                .build();
    }
    @Override
    public Entity.Address addressToGrpcAddress(AddressCustom address){
        return Entity.Address.newBuilder()
                .setBalance(address.getBalance())
                .setNonce(address.getNonce())
                .setPublicKey(address.getPublicKey())
                .setNoncePending(address.getNoncePending())
                .addAllHashTransactionsCompleted((address.getTransactionsComplete()==null)?new ArrayList<>():address.getTransactionsComplete())
                .build();
    }
    @Override
    public Entity.Block blockToGrpcBlock(Block<ArrayList<Transaction>> block) {
        ArrayList<Entity.Transaction> dataBlock = new ArrayList<>();
        for (Transaction transaction : block.getData()) {
            dataBlock.add(dataBlockToGrpcData(transaction));
        }
        return Entity.Block.newBuilder()
                .setBlockNumber(block.getBlockNumber())
                .setHash(block.getHash())
                .setBlockType(block.getBlockType()== BlockType.CONFIRMED? Entity.BlockType.CONFIRMED: Entity.BlockType.PENDING)
                .addAllData(dataBlock)
                .setNonce(block.getNonce())
                .setFeeRecipient(block.getFeeRecipient()==null ? "": block.getFeeRecipient())
                .setParentHash((block.getParentHash()==null) ? "": block.getParentHash())
                .build();
    }

    @Override
    public Transaction grpcDataToData(Entity.Transaction transaction) {
        return Transaction.newTransactionBuilder()
                .setTo(transaction.getTo())
                .setFrom(transaction.getFrom())
                .setBlockNumber(transaction.getBlockNumber())
                .setData(transaction.getData())
                .setGasPrice(transaction.getGasPrice())
                .setGas(transaction.getGas())
                .setStatus(transaction.getStatus())
                .setSing(transaction.getSing())
                .setHash(transaction.getHash())
                .setValue(transaction.getValue())
                .setNonce(transaction.getNonce())
                .build();
    }

    @Override
    public AddressCustom grpcAddressToAddress(Entity.Address address) {
        return  new AddressCustom().newAddressBuilder()
                .setBalance(address.getBalance())
                .setNonce(address.getNonce())
                .setPublicKey(address.getPublicKey())
                .setNoncePending(address.getNoncePending())
                .setHashTransactionComplete(new ArrayList<>(address.getHashTransactionsCompletedList()))
                .build();
    };


    @Override
    public Block<ArrayList<Transaction>> grpcBlockToBlock(Entity.Block block) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Entity.Transaction transaction : block.getDataList()) {
            transactions.add(grpcDataToData(transaction));
        }
        return new Block<ArrayList<Transaction>>().newBlockBuilder()
                .setBlockNumber(block.getBlockNumber())
                .setData(new ArrayList<>(transactions))
                .setHash(block.getHash())
                .setNonce(block.getNonce())
                .setBlockType(block.getBlockType()== Entity.BlockType.CONFIRMED?BlockType.CONFIRMED:BlockType.PENDING)
                .setFeeRecipient(block.getFeeRecipient())
                .setParentHash(block.getParentHash().isEmpty()?null:block.getParentHash()).build();
    }
    public ArrayList<Block<ArrayList<Transaction>>> convertAllGrpcBlock(List<Entity.Block> blocksGrpc){
        ArrayList<Block<ArrayList<Transaction>>> blocks = new ArrayList<>();
        for (Entity.Block block : blocksGrpc) {
            blocks.add(grpcBlockToBlock(block));
        }
        return blocks;
    }
    public ArrayList<Transaction> convertAllGrpcData(List<Entity.Transaction> transactionsGrpc){
        ArrayList<Transaction> transactions = new ArrayList<>();
        for (Entity.Transaction transaction : transactionsGrpc) {
            transactions.add(grpcDataToData(transaction));
        }
        return transactions;
    }
    public ArrayList<Entity.Block> convertAllBlock(ArrayList<Block<ArrayList<Transaction>>> blocks) {
        ArrayList<Entity.Block> blocksGrpc = new ArrayList<>();
        for (Block<ArrayList<Transaction>> block : blocks) {
            blocksGrpc.add(blockToGrpcBlock(block));
        }
        return blocksGrpc;
    }
    public ArrayList<Entity.Transaction> convertAllData(ArrayList<Transaction> transactions){
        ArrayList<Entity.Transaction> grpcTransaction = new ArrayList<>();
        for (Transaction transaction : transactions) {
            grpcTransaction.add(dataBlockToGrpcData(transaction));
        }
        return grpcTransaction;
    }


}





