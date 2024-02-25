package org.example.CustomBlockChain.Entity;

import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.AddressBuilder;

import java.util.ArrayList;

public class AddressCustomBuilder extends AddressBuilder {
    private ArrayList<String> hashTransactionComplete;
    public AddressCustomBuilder setHashTransactionComplete(ArrayList<String> hashes){
        this.hashTransactionComplete=hashes;
        return this;
    }
    @Override
    public AddressCustom build() {
        return new AddressCustom(super.publicKey,super.balance,hashTransactionComplete,super.nonce,super.noncePending);
    }
    @Override
    public AddressCustomBuilder setPublicKey(String publicKey) {
        super.setPublicKey(publicKey);
        return this;
    }
    @Override
    public AddressCustomBuilder setBalance(int balance) {
        super.setBalance(balance);
        return this;
    }
    @Override
    public AddressCustomBuilder setNoncePending(int noncePending) {
        super.setNoncePending(noncePending);
        return this;
    }
    @Override
    public AddressCustomBuilder setNonce(int nonce) {
        super.setNonce(nonce);
        return this;
    }
}
