package org.example.BlockChainBase.Rules;

import org.example.BlockChainBase.Entity.Block;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

public interface RuleBase<T> {

    boolean Execute(ArrayList<Block<T>> blocks, Block<T> newBlock) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException;
}
