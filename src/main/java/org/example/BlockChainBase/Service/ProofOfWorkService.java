package org.example.BlockChainBase.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.BlockChainBase.BlockChain.BlockChainBase;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.Entity.Address;
import org.example.BlockChainBase.Entity.Block;
import org.example.BlockChainBase.Exeptions.BlockChainException;
import org.example.BlockChainBase.Rules.PoWRule;
public class ProofOfWorkService<T> {
    private final BlockChainBase<T> blockChain;
    private final PoWRule<T> poWRule = new PoWRule<>();
    private final String feeRecipient;


    public ProofOfWorkService(BlockChainBase<T> blockChain, String feeRecipient) {
        this.blockChain = blockChain;
        this.feeRecipient = feeRecipient;
    }
    public void startMining(int height, Block<T> block) throws Exception {
        for (int i = 0; i < Integer.MAX_VALUE; i++)
        {
            if (poWRule.Execute(height, block.getHash())){
                block.setFeeRecipient(feeRecipient);
                System.out.println("Вы нашли: "+block.getHash());
                blockChain.addBlock(block);
                return;
            }
            nextVariant(block);
        }
        throw new BlockChainException("Block is not possible to build. Try again after some blocks will be put.");
    }

    private void nextVariant(Block<T> block) throws JsonProcessingException {
        block.setNonce(block.getNonce()+1);
        block.setHash(Block.calculateHash(block.getData(),block.getParentHash(),new HashEncoder(),block.getNonce()));
    }

}

