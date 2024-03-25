package org.example.CustomBlockChain.Entity;

public enum TypeUpdateRequestNodeCommunication {
    BLOCK,
    TRANSACTION_PENDING;

    public static TypeUpdateRequestNodeCommunication valueOf(int numberTypeRequest) {
        return switch (numberTypeRequest) {
            case 1 -> BLOCK;
            case 2 -> TRANSACTION_PENDING;
            default -> null;
        };
    }
}
