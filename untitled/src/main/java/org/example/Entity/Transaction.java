package org.example.Entity;

import java.io.ObjectInputFilter;

public class Transaction {

    public Transaction(Address from, int gas, int gasPrice, String sing, Address to, int value, String data) {
        this.from = from;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.sing = sing;

        this.to = to;
        this.value = value;
        this.data = data;
    }

    Address from;
    int gas;
    int blockNumber;
    TransactionStatus status;
    int gasPrice;
    String sing;
    Address to;
    int value;
    String data;

    public Address getFrom() {
        return from;
    }

    public void setFrom(Address from) {
        this.from = from;
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

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
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



    public Address getTo() {
        return to;
    }

    public void setTo(Address to) {
        this.to = to;
    }



    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

