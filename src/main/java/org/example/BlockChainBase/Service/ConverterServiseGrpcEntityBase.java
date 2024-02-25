package org.example.BlockChainBase.Service;


import java.io.IOException;

public interface ConverterServiseGrpcEntityBase<GrpcData,Data,GrpcAddress,Address,GrpcBlock,Block> {
    GrpcData dataBlockToGrpcData(Data data);
    GrpcAddress addressToGrpcAddress(Address address);
    GrpcBlock blockToGrpcBlock(Block block) throws IOException;

    Data grpcDataToData(GrpcData grpcData);
    Address grpcAddressToAddress(GrpcAddress grpcAddress);
    Block grpcBlockToBlock(GrpcBlock block);
    }
