package org.example.BlockChainBase.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
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

    boolean isValid(Block<T> blocks, String tail) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException;

    public boolean isQueryValid(String lastHash, int height, BlockChainInfoBD.BlockChainInfoStruct blockChainInfoStruct);
    void scanBlockChain() throws Exception;
    boolean isValidAllBlock(ArrayList<Block<T>> blocks) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException;

    void addAll(ArrayList<Block<T>> blocks) throws Exception;
    void addAllToBlockPoll(ArrayList<Block<T>> blocks) throws Exception;
    String getTailFromBlockPoll();
}
