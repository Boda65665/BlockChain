package org.example.CustomBlockChain.Entity;

import org.example.BlockChainBase.Entity.Address;

public class Transaction {


    public Transaction(String from, int gas, int gasPrice, String sing, String to, int value, String data, int nonce, String hash) {
        this.from = from;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.sing = sing;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
    }


    public Transaction(String from, int gas, int blockNumber, int gasPrice, String sing, String to, int value, String data, int nonce, String hash, boolean status) {
        this.from = from;
        this.gas = gas;
        this.blockNumber = blockNumber;
        this.gasPrice = gasPrice;
        this.sing = sing;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
        this.hash = hash;
        this.status = status;
    }

    public Transaction() {
    }


    private String from;
    private int gas;
    private int blockNumber;
    private int gasPrice;
    private String sing;
    private String to;
    private int value;
    private String data;
    private int nonce;

    private String hash;
    private boolean status;





    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public int getGas() {
        return gas;
    }

    public void setGas(int gas) {
        this.gas = gas;
    }

    public int getBlockNumber() {
        return blockNumber;
    }

    public void setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
    }

    public int getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getSing() {
        return sing;
    }

    public void setSing(String sing) {
        this.sing = sing;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
    public static TransactionBuilder newTransactionBuilder(){
        return new TransactionBuilder();
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "from=" + from +
                ", gas=" + gas +
                ", blockNumber=" + blockNumber +
                ", gasPrice=" + gasPrice +
                ", sing='" + sing + '\'' +
                ", to=" + to +
                ", value=" + value +
                ", data='" + data + '\'' +
                ", nonce=" + nonce +
                ", hash='" + hash + '\'' +
                ", status=" + status +
                '}';
    }
}

