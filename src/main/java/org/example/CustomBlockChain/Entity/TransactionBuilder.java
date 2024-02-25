package org.example.CustomBlockChain.Entity;

import org.example.BlockChainBase.Entity.Address;

public class TransactionBuilder {
    private AddressCustom from;
    private int gas;
    private int blockNumber;
    private int gasPrice;
    private String sing;
    private AddressCustom to;
    private int value;
    private String data;
    private int nonce;
    private String hash;
    private boolean status;

    public TransactionBuilder setFrom(AddressCustom from) {
        this.from = from;
        return this;

    }

    public TransactionBuilder setGas(int gas) {
        this.gas = gas;
        return this;

    }

    public TransactionBuilder setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
        return this;

    }

    public TransactionBuilder setGasPrice(int gasPrice) {
        this.gasPrice = gasPrice;
        return this;

    }

    public TransactionBuilder setSing(String sing) {
        this.sing = sing;
        return this;

    }

    public TransactionBuilder setTo(AddressCustom to) {
        this.to = to;
        return this;

    }

    public TransactionBuilder setValue(int value) {
        this.value = value;
        return this;

    }

    public TransactionBuilder setData(String data) {
        this.data = data;
        return this;

    }

    public TransactionBuilder setNonce(int nonce) {
        this.nonce = nonce;
        return this;

    }

    public TransactionBuilder setHash(String hash) {
        this.hash = hash;
        return this;

    }

    public TransactionBuilder setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public Transaction build(){
        return new Transaction(from,gas,gasPrice,sing,to,value,data,nonce,hash);
    }
}
