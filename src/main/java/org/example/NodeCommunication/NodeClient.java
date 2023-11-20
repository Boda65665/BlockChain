package org.example.NodeCommunication;

import org.bouncycastle.jcajce.provider.symmetric.util.BaseBlockCipher;
import org.example.BlockChain.BlockChainBase;
import org.example.Entity.Block;
import org.example.JavaChain;
import org.example.LevelDb.Block.LevelDbBlock;
import org.example.LevelDb.State.LevelDbState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class NodeClient<T> {
    LevelDbBlock<Block<T>> levelDbBlock = new LevelDbBlock<>();
    LevelDbState levelDbState = new LevelDbState();
    private final BlockChainBase<T> blockChain;

    public NodeClient(BlockChainBase<T> blockChain) {
        this.blockChain = blockChain;
    }

    public void SynchronizationBlockChain(String IP) throws IOException {
        Socket socket = new Socket(IP,7880);
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println("connect to "+IP+":"+7880);
        System.out.println("Starting download blockChain");
        out.println("full");
        if (blockChain.getBlocks().isEmpty()) out.println(blockChain.getBlocks().get(blockChain.getBlocks().size()-1));
        out.flush();
        String blocksJson = in.readLine();
        String poolBlocksJson = in.readLine();
        String statesJson = in.readLine();
        levelDbBlock.buildBlockChain(blocksJson,poolBlocksJson);
        levelDbState.buildStates(statesJson);
        out.close();
        in.close();
        socket.close();
    }
}
