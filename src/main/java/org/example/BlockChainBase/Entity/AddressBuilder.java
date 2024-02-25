package org.example.BlockChainBase.Entity;

public class AddressBuilder {
    public String publicKey;
    public int balance;
    //nonce transaction
    public int nonce;
    //nonce transaction which has not yet been confirmed
    public int noncePending;

    public AddressBuilder setPublicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public AddressBuilder setBalance(int balance) {
        this.balance = balance;
        return this;
    }

    public AddressBuilder setNoncePending(int noncePending) {
        this.noncePending = noncePending;
        return this;
    }

    public AddressBuilder setNonce(int nonce) {
        this.nonce = nonce;
        return this;
    }

    public Address build(){
        return new Address(publicKey,balance,noncePending,nonce);
    }
}
