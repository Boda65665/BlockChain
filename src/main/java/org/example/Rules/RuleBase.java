package org.example.Rules;

import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.ArrayList;

public interface RuleBase<T> {

    void Execute(ArrayList<Block<T>> blocks, Block<T> newBlock) throws BlockChainException, IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException;
}
