package org.example.CustomBlockChain.Entity;

public class Wallet {
    String password;
    String publicKey;
    String privateKey;

    public Wallet(String password, String publicKey, String privateKey) {
        this.password = password;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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



}
