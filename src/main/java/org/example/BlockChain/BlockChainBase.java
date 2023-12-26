package org.example.BlockChain;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.checkerframework.checker.units.qual.A;
import org.example.Entity.Address;
import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;

public interface BlockChainBase<T> {
    void addBlock(Block<T> block) throws Exception;
    ArrayList<Block<T>> getBlocks();
    ArrayList<Block<T>> getBlocksStartingFrom(String hash);
    ArrayList<Address> getAddressStartingFrom(String publicKey);
    ArrayDeque<Block<T>> getBlocksPool();
    ArrayList<Address> getAllAddresses();
    void addBlockToPoll(Block<T> block) throws Exception;

}
