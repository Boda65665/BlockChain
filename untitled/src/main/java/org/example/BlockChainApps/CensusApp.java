//package org.example.BlockChainApps;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import org.example.BlockChain.BlockChain;
//import org.example.Cryptography.HashEncoder;
//import org.example.Entity.Block;
//import org.example.Exeptions.BlockChainException;
//import org.example.Service.BuildBlockChainService;
//import org.example.BlockChain.CensusChain;
//
//public class CensusApp {
//
//
//
//    public static void main(String[] args) throws JsonProcessingException, BlockChainException
//    {
//        final HashEncoder hashEncoder = new HashEncoder();
//        String[] names = {"Bob","Bogdan","Bob","ld","Vt","Vft"};
//        BlockChain<String> blockChain = new BlockChain<>(hashEncoder);
//        BuildBlockChainService<String> buildBlockService = new BuildBlockChainService<>();
//        CensusChain censusChain = new CensusChain(blockChain);
//        for (String name : names) {
//            Block<String> block = buildBlockService.buildBlock(name);
//            censusChain.addBlock(block);
//            buildBlockService.acceptBlock(block);
//
//        }
//        System.out.println(censusChain.toString());
//
//
//
//    }
//}
