package org.example.Entity;

import org.example.Cryptography.Asymmetric;

import java.util.ArrayList;

public class Address {

    public Address(Asymmetric.Keys keys, int balance, ArrayList<Transaction> transactionsComplete) {
        this.publicKey = keys.publicKey();
        this.privateKey = keys.privateKey();
        this.balance = balance;
        this.transactionsComplete = transactionsComplete;
    }
    public Address(Asymmetric.Keys keys){
        this.publicKey = keys.publicKey();
        this.privateKey = keys.privateKey();
        this.balance = 0;
        this.transactionsComplete = new ArrayList<>();
    }

    private String publicKey;
    private String privateKey;
    private int balance;
    private ArrayList<Transaction> transactionsComplete;

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<Transaction> getTransactionsComplete() {
        return transactionsComplete;
    }

    public void setTransactionsComplete(ArrayList<Transaction> transactionsComplete) {
        this.transactionsComplete = transactionsComplete;
    }
}
