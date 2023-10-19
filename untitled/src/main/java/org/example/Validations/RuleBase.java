package org.example.Validations;

import org.example.Entity.Block;
import org.example.Exeptions.BlockChainException;

import java.util.ArrayList;

public interface RuleBase<T> {
    public void Execute(ArrayList<Block<T>> blocks, Block<T> newBlock);
}
