package org.example.Entity;

import com.google.gson.Gson;

import java.util.ArrayList;

public class Transaction {


    public Transaction(Address from, int gas, int gasPrice, String sing, Address to, int value, String data, int nonce,String hash) {
        this.from = from;
        this.gas = gas;
        this.gasPrice = gasPrice;
        this.sing = sing;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
    }

    public Transaction(Address from, int gas, int blockNumber, int gasPrice, String sing, Address to, int value, String data, int nonce,boolean status) {
        this.from = from;
        this.gas = gas;
        this.blockNumber = blockNumber;
        this.gasPrice = gasPrice;
        this.sing = sing;
        this.to = to;
        this.value = value;
        this.data = data;
        this.nonce = nonce;
        this.status = status;
    }

    public Transaction() {
    }


    private Address from;
    private int gas;
    private int blockNumber;
    private int gasPrice;
    private String sing;
    private Address to;
    private int value;
    private String data;
    private int nonce;

    private String hash;

    public Address getFrom() {
        return from;
    }
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
}

