package org.example.BlockChainBase.Cryptography;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AESEncryption {

    private static final String key = "wegtgelxgofke$dk"; // 16 символов для AES-128
    SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

    public AESEncryption() throws UnsupportedEncodingException {
    }

//    public static void main(String[] args) {
//        try {
//            String ipAddress = getLocalIpAddress();
//            System.out.println("Original IP address: " + ipAddress);
//
//            String encryptedIp = encode(ipAddress);
//            System.out.println("Encrypted IP address: " + encryptedIp);
//
//            String decryptedIp = decode(encryptedIp);
//            System.out.println("Decrypted IP address: " + decryptedIp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static String getLocalIpAddress() throws Exception {
//        return InetAddress.getLocalHost().getHostAddress();
//    }

    public  String encode(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedValue = cipher.doFinal(value.getBytes());
        return Base64.getEncoder().encodeToString(encryptedValue);
    }

    public  String decode(String value) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedValue = cipher.doFinal(Base64.getDecoder().decode(value));
        return new String(decryptedValue);
    }

//    public static void main(String[] args) throws UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
//        AESEncryption aesEncryption = new AESEncryption();
//        Wallet wallet = new Wallet("df","","dwa","dwd");
//        wallet.setPrivateKey(aesEncryption.encode(wallet.getPrivateKey()));
//        wallet.setSecretPhrase(aesEncryption.encode(wallet.getSecretPhrase()));
//        String encode = aesEncryption.encode("gg");
//        System.out.println(aesEncryption.decode("/X0BTt8skxplUGo1/zGz2M0/1bWO670w7AEVbfp8bHjNOuqc9tkH7x4F3803m3Yu10JEJea7U/t2EmeHPGlyzi3g8agqTfVewn2FspoEwNeVkV0aHRk2KYksV4CplPOPNNISzlQnzJ58iB11JoMCZg=="));
//    }
}