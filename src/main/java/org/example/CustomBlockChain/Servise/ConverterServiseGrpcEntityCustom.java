package org.example.CustomBlockChain.Servise;

import node.entity.Entity;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Service.ConverterServiseGrpcEntityBase;
import org.example.CustomBlockChain.DB.LevelDB.State.LevelDBStateCustom;
import org.example.CustomBlockChain.Entity.AddressCustom;
import org.example.CustomBlockChain.Entity.Transaction;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ConverterServiseGrpcEntityCustom implements ConverterServiseGrpcEntityBase<Entity.Transaction,Transaction, Entity.Address,AddressCustom,
        Entity.Block,Block<ArrayList<Transaction>>> {
    LevelDBStateCustom levelDBStateCustom = new LevelDBStateCustom();
    public ConverterServiseGrpcEntityCustom() throws SQLException, IOException, ClassNotFoundException {
    }


    @Override
    public Entity.Transaction dataBlockToGrpcData(Transaction transaction){
        return Entity.Transaction.newBuilder()
                .setTo(addressToGrpcAddress(transaction.getTo()))
                .setFrom(addressToGrpcAddress(transaction.getFrom()))
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
    public Entity.Block blockToGrpcBlock(Block<ArrayList<Transaction>> block) throws IOException {
        ArrayList<Entity.Transaction> dataBlock = new ArrayList<>();
        for (Transaction transaction : block.getData()) {
            dataBlock.add(dataBlockToGrpcData(transaction));
        }
        String parentHash = (block.getParentHash()==null) ? "": block.getParentHash();
        return Entity.Block.newBuilder()
                .setBlockNumber(block.getBlockNumber())
                .setHash(block.getHash())
                .addAllData(dataBlock)
                .setNonce(block.getNonce())
                .setFeeRecipient(addressToGrpcAddress(levelDBStateCustom.get(block.getFeeRecipient().getPublicKey())))
                .setParentHash(parentHash)
                .build();
    }

    @Override
    public Transaction grpcDataToData(Entity.Transaction transaction) {
        return Transaction.newTransactionBuilder()
                .setTo(grpcAddressToAddress(transaction.getTo()))
                .setFrom(grpcAddressToAddress(transaction.getFrom()))
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
                .setFeeRecipient(grpcAddressToAddress(block.getFeeRecipient()))
                .setParentHash(block.getParentHash()).build();
    }



}





