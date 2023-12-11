package org.example.Cryptography;// Java program to create a
// asymmetric key

import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.K;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.*;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.spec.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.zip.DeflaterOutputStream;

// Class to create an asymmetric key
public  class Asymmetric {
    public record Keys(String publicKey, String privateKey) {
        public Keys(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    public Keys generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {


        Security.setProperty("crypto.policy", "unlimited");
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(512); // Установка размера ключа
        // Генерация ключевой пары
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // Convert keys to strings
        String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        return new Keys(publicKeyString, privateKeyString);
    }

    public String sign(String message, String privateKeyString) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeyException | SignatureException |
                 InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }



    public boolean verify(String message, String publicKeyString, String signatureString) {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initVerify(publicKey);
            signature.update(message.getBytes());
            return signature.verify(signatureBytes);
        } catch (IllegalArgumentException | NoSuchAlgorithmException  |
                 InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;

        }
    }
    public boolean isValidPublicKey(String publicKeyString){
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        try {
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            return true;
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            return false;
        }
    }
    public String generatePublicKeyFromPrivateKey(String privateKeyString)  {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            RSAPrivateCrtKey privk = (RSAPrivateCrtKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            RSAPublicKeySpec publicKeySpec = new java.security.spec.RSAPublicKeySpec(privk.getModulus(), privk.getPublicExponent());
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey myPublicKey = keyFactory.generatePublic(publicKeySpec);
            byte[] publicKeyBytes = myPublicKey.getEncoded();
            return Base64.getEncoder().encodeToString(publicKeyBytes);
        }
        catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e){
            return null;
        }

    }


        }
