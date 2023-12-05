package org.example.Entity;

public class Wallet {
    String password;
    String publicKey;
    String privateKey;
    String secretPhrase;

    public Wallet(String password, String publicKey, String privateKey, String secretPhrase) {
        this.password = password;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.secretPhrase = secretPhrase;
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

    public String getSecretPhrase() {
        return secretPhrase;
    }

    public void setSecretPhrase(String secretPhrase) {
        this.secretPhrase = secretPhrase;
    }
}
