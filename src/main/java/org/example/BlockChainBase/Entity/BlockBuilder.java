package org.example.BlockChainBase.Entity;

public class BlockBuilder<T> {
    private String parentHash;
    private Address feeRecipient;
    private String hash;
    int blockNumber;
    T data;
    int nonce;

    public BlockBuilder<T> setParentHash(String parentHash) {
        this.parentHash = parentHash;
        return this;}

    public BlockBuilder<T> setFeeRecipient(Address feeRecipient) {
        this.feeRecipient = feeRecipient;
        return this;}

    public BlockBuilder<T> setHash(String hash) {
        this.hash = hash;
        return this;}

    public BlockBuilder<T> setBlockNumber(int blockNumber) {
        this.blockNumber = blockNumber;
        return this;}

    public BlockBuilder<T> setData(T data) {
        this.data = data;
        return this;}

    public BlockBuilder<T> setNonce(int nonce) {
        this.nonce = nonce;
        return this;}
    public Block<T> build(){
        return new Block<T>(parentHash,feeRecipient,hash,blockNumber,data,nonce);
    }
}
