package org.example.Entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Cryptography.HashEncoder;

import java.util.ArrayList;

public class Block<T> {
    public Block(String parentHash, String hash, int blockNumber, T data) {
        this.parentHash = parentHash;
        this.hash = hash;
        this.blockNumber = blockNumber;
        this.data = data;
    }

    public Block(T data) {
        this.data = data;
    }

    private String parentHash;
    private String feeRecipient;
    private String hash;
    int blockNumber;
    T data;
    int nonce;

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getParentHash() {
        return parentHash;
    }

    public void setParentHash(String parentHash) {
        this.parentHash = parentHash;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public String getFeeRecipient() {
        return feeRecipient;
    }

    public void setFeeRecipient(String feeRecipient) {
        this.feeRecipient = feeRecipient;
    }

    static public String calculateHash(Object objData, String parentHash, HashEncoder hashEncoder, int nonce) throws JsonProcessingException {
        String data = transactionsToJson(objData)+nonce;
        return hashEncoder.SHA256(data+parentHash);
    }
    static private String transactionsToJson(Object objData) throws JsonProcessingException {
        ObjectMapper jsonObject = new ObjectMapper();
        return jsonObject.writeValueAsString(objData);

    }

}



