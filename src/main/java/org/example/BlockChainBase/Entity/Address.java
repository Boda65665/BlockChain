package org.example.BlockChainBase.Entity;


import java.util.ArrayList;

public class Address {

    public Address(String publicKey, int balance,int noncePending,int nonce) {
        this.publicKey = publicKey;
        this.balance = balance;
        this.nonce = nonce;
        this.noncePending=noncePending;
    }
    public Address(String publicKey){
        this.publicKey = publicKey;
        this.balance = 0;
        nonce = 0;
    }

    public Address() {
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    private String publicKey;
    private int balance;
    //nonce transaction
    private int nonce;
    //nonce transaction which has not yet been confirmed
    private int noncePending;

    public String getPublicKey() {
        return publicKey;
    }


    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }


    public int getNoncePending() {
        return noncePending;
    }

    public void setNoncePending(int noncePending) {
        this.noncePending = noncePending;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public AddressBuilder newAddressBuilder(){
        return new AddressBuilder();
    }

    @Override
    public String toString() {
        return "Address{" +
                "publicKey='" + publicKey + '\'' +
                ", balance=" + balance +
                ", nonce=" + nonce +
                ", noncePending=" + noncePending+'}';
    }
}
