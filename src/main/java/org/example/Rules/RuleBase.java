package org.example.Rules;

import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public interface RuleBase<T> {

    void Execute(ArrayList<Block<T>> blocks, Block<T> newBlock) throws BlockChainException ;
}
