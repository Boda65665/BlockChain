package org.example.BlockChainBase.BlockChain;

import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Exeptions.BlockChainException;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public interface BlockChainBase<T> {
    void addBlock(Block<T> block) throws Exception;
    ArrayList<Block<T>> getBlocks();
    ArrayList<Block<T>> getBlocksStartingFrom(int numberBlock);
    ArrayDeque<Block<T>> getBlocksPool();
    ArrayList<Address> getAllAddresses();
    void addBlockToPoll(Block<T> block) throws Exception;

    boolean isValid(ArrayList<Block<T>> blocks) throws IOException, BlockChainException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException;

    boolean isQueryValid();

    void scanBlockChain() throws Exception;

    void addAll(ArrayList<Block<T>> blocks) throws Exception;
    String getTailFromBlockPoll();
}
