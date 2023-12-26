package org.example.Entity;


import java.util.ArrayList;

public class Address {

    public Address(String publicKey, int balance, ArrayList<String> transactionsComplete,int nonce) {
        this.publicKey = publicKey;
        this.balance = balance;
        this.transactionsComplete = transactionsComplete;
        this.nonce = nonce;

    }
    public Address(String publicKey){
        this.publicKey = publicKey;
        this.balance = 0;
        this.transactionsComplete = new ArrayList<>();
        nonce = 0;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    private String publicKey;
    private int balance;
    private int nonce;
    private ArrayList<String> transactionsComplete = new ArrayList<>();

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }



    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public ArrayList<String> getTransactionsComplete() {
        return transactionsComplete;
    }

    public void setTransactionsComplete(ArrayList<String> transactionsComplete) {
        this.transactionsComplete = transactionsComplete;
    }
}
