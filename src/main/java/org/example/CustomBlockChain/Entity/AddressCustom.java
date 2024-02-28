package org.example.CustomBlockChain.Entity;

import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.AddressBuilder;

import java.util.ArrayList;

public class AddressCustom extends Address {
    private ArrayList<String> transactionsComplete;

    public AddressCustom(String publicKey, int balance, ArrayList<String> transactionsComplete, int nonce,int noncePending) {
        super(publicKey, balance, noncePending,nonce);
        this.transactionsComplete=transactionsComplete;
    }

    public AddressCustom() {
        super();
    }

    public AddressCustom(String publicKey) {
        super(publicKey);
    }

    public ArrayList<String> getTransactionsComplete() {
        return transactionsComplete;
    }

    public void setTransactionsComplete(ArrayList<String> transactionsComplete) {
        this.transactionsComplete = transactionsComplete;
    }
    @Override
    public AddressCustomBuilder newAddressBuilder(){
        return new AddressCustomBuilder();
    }


}
