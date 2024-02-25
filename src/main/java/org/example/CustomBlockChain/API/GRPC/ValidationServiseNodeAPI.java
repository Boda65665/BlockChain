package org.example.CustomBlockChain.API.GRPC;


import com.google.protobuf.Any;
import com.google.protobuf.Timestamp;
import com.google.rpc.Code;

import com.google.rpc.Status;
import node.api.grc.NodeApi.*;
import org.example.BlockChainBase.Cryptography.Asymmetric;
import org.example.BlockChainBase.Cryptography.HashEncoder;
import org.example.BlockChainBase.Entity.Address;
import org.example.CustomBlockChain.Servise.JavaChainMethodService;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.time.Instant;

public class ValidationServiseNodeAPI {
    private final JavaChainMethodService javaChainMethodService;
    Asymmetric asymmetricEncoder = new Asymmetric();
    HashEncoder hashEncoder = new HashEncoder();

    public ValidationServiseNodeAPI(JavaChainMethodService javaChainMethodService) {
        this.javaChainMethodService = javaChainMethodService;
    }

    public Status validGetBalanceRequest(GetBalanceRequest request) throws IOException {
        if (request.getAddress().isEmpty()) {
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.ADDRESS_CANNOT_BE_NULL).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("address can be null")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        if (javaChainMethodService.getAddress(request.getAddress())==null){
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.NOT_FOUND_THIS_ADDRESS).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("this address does not exist")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        return null;
    }

    public Status validGetBlockRequest(GetBlockRequest request) throws IOException {
        long numberBlock = request.getNumber();
        String hash = request.getHash();

        if (hash.isEmpty() && numberBlock==0) {
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.ID_BLOCK_CANNOT_BE_NULL).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("id block can be null")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }

        //If the user did not pass the argument (hash), then it is null, and in this case we need to assign the value "" so as not to get an error
        if (hash.isEmpty()){
            hash = "" ;
        }

            if (javaChainMethodService.getBlockByHash(hash)==null  && javaChainMethodService.getBlockByNumber(request.getNumber())==null){
                ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.NOT_FOUND_THIS_BLOCK).setTimestamp(getTimeNow()).build();
                return Status.newBuilder()
                        .setCode(Code.NOT_FOUND.getNumber())
                        .setMessage("this hash does not exist")
                        .addDetails(Any.pack(exceptionResponse))
                        .build();

        }
        return null;
    }

    public Status validSendTransactionRequest(SendTransactionRequest request) throws IOException, SignatureException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException {
        Transaction transaction = request.getTransactionStruct();
        if (transaction.getFrom().isEmpty() || transaction.getTo().isEmpty() || transaction.getFrom().isEmpty() || transaction.getValue()==0 || request.getSign().isEmpty() || transaction.getGas()==0 || transaction.getGasPrice()==0)
        {
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.ARGUMENT_CANNOT_BE_NULL).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("argument cannot be null")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        String sign = request.getSign();
        Address from = javaChainMethodService.getAddress(transaction.getFrom());
        if (from==null) {
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.NOT_FOUND_THIS_ADDRESS).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("Address no found")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        };
        if (from.getBalance()<transaction.getValue()){
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.INSUFFICIENT_FOUNDS).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("Insufficient funds")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        if (!asymmetricEncoder.verify(hashEncoder.SHA256(transaction.getTo()+javaChainMethodService.getNoncePending(transaction.getFrom())), from.getPublicKey(), sign)){
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.INCORRECT_SIGNATURE).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("Incorrect signature")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.INSUFFICIENT_FOUNDS).setTimestamp(getTimeNow()).build();
        return null;
//
    }

    public Status validGetTransactionRequest(GetTransactionRequest request) throws IOException {
        if (request.getHash().isEmpty()){
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.ARGUMENT_CANNOT_BE_NULL).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.INVALID_ARGUMENT.getNumber())
                    .setMessage("Incorrect signature")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        if (javaChainMethodService.getTransaction(request.getHash())==null){
            ExceptionResponse exceptionResponse = ExceptionResponse.newBuilder().setErrorCode(ErrorCod.NOT_FOUND_THIS_TRANSACTION).setTimestamp(getTimeNow()).build();
            return Status.newBuilder()
                    .setCode(Code.NOT_FOUND.getNumber())
                    .setMessage("Not found this transaction")
                    .addDetails(Any.pack(exceptionResponse))
                    .build();
        }
        return null;
    }
    
    public Timestamp getTimeNow(){
        Instant time = Instant.now();
        Timestamp timestamp = Timestamp.newBuilder().setSeconds(time.getEpochSecond())
                .setNanos(time.getNano()).build();
        return timestamp;
    }
}
