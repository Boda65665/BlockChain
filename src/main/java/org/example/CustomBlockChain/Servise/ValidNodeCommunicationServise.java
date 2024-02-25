package org.example.CustomBlockChain.Servise;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import node.communication.base.NodeCommunicationServer;
import org.example.BlockChainBase.BlockChain.BlockChainBase;
import org.example.CustomBlockChain.Entity.Transaction;

import java.time.Instant;
import java.util.ArrayList;

public class ValidNodeCommunicationServise {
    private final BlockChainBase<ArrayList<Transaction>> blockChain;
    public Timestamp getTimeNow(){
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return timestamp;
    }

    public ValidNodeCommunicationServise(BlockChainBase<ArrayList<Transaction>> blockChain) {
        this.blockChain = blockChain;
    }

    public Status validDownloadRequest(NodeCommunicationServer.DownloadRequest request) {
        if (request.getLastNumberBlock()<0) {
            NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.INVALID_NUMBER_BLOCK).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("number block can be null")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        if (request.getLastNumberBlock()>blockChain.getBlocks().size()-1){
            NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.NOT_FOUND).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("size blocks this node is "+blockChain.getBlocks().size())
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        return null;
    }
}
