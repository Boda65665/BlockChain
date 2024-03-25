package org.example.CustomBlockChain.Servise.Validation;

import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;
import com.google.rpc.Status;
import node.communication.base.NodeCommunicationServer;
import node.entity.Entity;
import org.example.BlockChainBase.Cryptography.AESEncryption;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.DB.SQL.BlockChainInfo.BlockChainInfoBD;
import org.example.CustomBlockChain.BlockChain.JavaChain;
import org.example.CustomBlockChain.Entity.Transaction;
import org.example.CustomBlockChain.Entity.TypeDownloadRequestNodeCommunication;
import org.example.CustomBlockChain.Entity.TypeUpdateRequestNodeCommunication;
import org.example.CustomBlockChain.Rules.TransactionRule;
import org.example.CustomBlockChain.Servise.ConverterServiseGrpcEntityCustom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.time.Instant;

public class ValidNodeCommunicationServise {
    private final JavaChain blockChain;
    AESEncryption encryption = new AESEncryption();
    TransactionRule transactionRule = new TransactionRule();
    BlockChainInfoBD blockChainInfoBD = new BlockChainInfoBD();
    private final ConverterServiseGrpcEntityCustom converterServiseGrpcEntityCustom = new ConverterServiseGrpcEntityCustom();
    public Timestamp getTimeNow(){
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return timestamp;
    }

    public ValidNodeCommunicationServise(JavaChain blockChain) throws SQLException, IOException, ClassNotFoundException {
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
        if (request.getLastNumberBlock()>blockChain.getBlocks().size()-1 && TypeDownloadRequestNodeCommunication.valueOf(request.getType())== TypeDownloadRequestNodeCommunication.ALL){
            NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.NOT_FOUND).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("size blocks this node is "+blockChain.getBlocks().size())
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        return null;
    }
    public Status validUpdateRequest(NodeCommunicationServer.UpdateRequest request) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        TypeUpdateRequestNodeCommunication typeRequest = TypeUpdateRequestNodeCommunication.valueOf(request.getDataCase().getNumber());
        if (!request.hasBlockChainInfo()){
            NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.DATA_BE_NULL).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("blockChainInfo cannot be null")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }

        switch (typeRequest){
            case BLOCK:{
                Entity.Block block = request.getBlock();
                try {
                    if (!blockChain.isValid(converterServiseGrpcEntityCustom.grpcBlockToBlock(block),blockChainInfoBD.getBlockChainInfo().lastHsh())){
                        NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder()
                                .setErrorCode(NodeCommunicationServer.ErrorCod.INCORRECT_DATA)
                                .setTimestamp(getTimeNow())
                                .build();
                        return Status.newBuilder()
                                .setCode(Code.INVALID_ARGUMENT.getNumber())
                                .setMessage("INCORRECT BLOCK")
                                .addDetails(Any.pack(exceptionResponse))
                                .build();
                    }
                } catch (Exception e) {
                  e.printStackTrace();
                }
                break;

            }


            case TRANSACTION_PENDING:{
                Transaction transaction = converterServiseGrpcEntityCustom.grpcDataToData(request.getTransaction());
                if (!transactionRule.Execute(transaction)){
                    NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.INCORRECT_DATA).setTimestamp(getTimeNow()).build();
                    return Status.newBuilder()
                            .setCode(Code.INVALID_ARGUMENT.getNumber())
                            .setMessage("INCORRECT TRANSACTION")
                            .addDetails(Any.pack(exceptionResponse))
                            .build();
                }
                }
                break;

            case null:{
                NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.DATA_BE_NULL).setTimestamp(getTimeNow()).build();
                return Status.newBuilder()
                        .setCode(Code.INVALID_ARGUMENT.getNumber())
                        .setMessage("update data cannot be null")
                        .addDetails(Any.pack(exceptionResponse))
                        .build();
            }

        }


        return null;

    }
    public Status validIsUpdateStatus(NodeCommunicationServer.IsUpdateRequest request) throws Exception {
        TypeUpdateRequestNodeCommunication typeRequest = TypeUpdateRequestNodeCommunication.valueOf(request.getInfoAboutLastDataCase().getNumber());
        switch (typeRequest){

            case BLOCK:
            {
                if (blockChain.isAlreadyExistBlock(request.getLastNumberBlock())){
                    NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.ALREADY_EXISTS).setTimestamp(getTimeNow()).build();
                    return Status.newBuilder()
                            .setCode(Code.ALREADY_EXISTS.getNumber())
                            .setMessage("ALREADY_EXISTS")
                            .addDetails(Any.pack(exceptionResponse))
                            .build();
                }
                break;
            }

            case TRANSACTION_PENDING:
        {
                if (blockChain.isAlreadyExistTransactionPending(request.getLastTransactionHash())){
                    NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.ALREADY_EXISTS).setTimestamp(getTimeNow()).build();
                    return Status.newBuilder()
                            .setCode(Code.ALREADY_EXISTS.getNumber())
                            .setMessage("ALREADY_EXISTS")
                            .addDetails(Any.pack(exceptionResponse))
                            .build();
                }
                break;
            }
            case null:
            {
                NodeCommunicationServer.ExceptionResponse exceptionResponse = NodeCommunicationServer.ExceptionResponse.newBuilder().setErrorCode(NodeCommunicationServer.ErrorCod.DATA_BE_NULL).setTimestamp(getTimeNow()).build();
                return Status.newBuilder()
                        .setCode(Code.INVALID_ARGUMENT.getNumber())
                        .setMessage("update data cannot be null")
                        .addDetails(Any.pack(exceptionResponse))
                        .build();
            }

        }
        return null;
    }
}
