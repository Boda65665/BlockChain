package org.example.BlockChainBase.Cryptography;// Java program to create a
// asymmetric key
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.interfaces.ECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

public class Asymmetric {
    public boolean isValidPublicKey(String to) {
        return getPublicKeyFromString(to)!=null;
    }

    public  record Keys(String publicKey, String privateKey) {
        public  Keys(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }



    public String sign(String plaintext, String privateKey) throws SignatureException, UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
        Security.addProvider(new BouncyCastleProvider());

        Signature ecdsaSign = Signature.getInstance("SHA256withECDSA", "BC");
        ecdsaSign.initSign(getPrivateKeyFromString(privateKey));
        ecdsaSign.update(plaintext.getBytes("UTF-8"));
        byte[] signature = ecdsaSign.sign();
        return Base64.getEncoder().encodeToString(signature);

    }

    public  boolean verify(String plaintext, String publicKey,String signature) throws SignatureException,
            InvalidKeyException, UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());

        Signature ecdsaVerify = Signature.getInstance("SHA256withECDSA",
                "BC");
        ecdsaVerify.initVerify(getPublicKeyFromString(publicKey));
        ecdsaVerify.update(plaintext.getBytes("UTF-8"));
        return ecdsaVerify.verify(Base64.getDecoder().decode(signature));
    }

    public  Keys generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        Security.addProvider(new BouncyCastleProvider());

        // Используем кривую с меньшей длиной ключа для минимизации размера ключей
        ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp192r1");
        Security.addProvider(new BouncyCastleProvider());

        KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", "BC");

        g.initialize(ecSpec);
        KeyPair keyPair = g.generateKeyPair();
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();

// Конвертация в строковое представление в формате Base64
        String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);
        String privateKeyString = Base64.getEncoder().encodeToString(privateKeyBytes);
        return new Keys(publicKeyString,privateKeyString);
    }
    public String getPublicFromPrivateKey(String privateKey) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeySpecException {
            KeyFactory keyFactory;
        Security.addProvider(new BouncyCastleProvider());

        keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("secp192r1");
            ECPoint pointQ = ecSpec.getG().multiply(((ECPrivateKey) getPrivateKeyFromString(privateKey)).getD());
            ECPublicKeySpec pubSpec = new ECPublicKeySpec(pointQ, ecSpec);
            byte[] publicKeyBytes = keyFactory.generatePublic(pubSpec).getEncoded();
        return Base64.getEncoder().encodeToString(publicKeyBytes);
    }
    private   PrivateKey getPrivateKeyFromString(String privateKeyString) throws InvalidKeySpecException {
        Security.addProvider(new BouncyCastleProvider());

        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
        }
        return null;
    }
    private  PublicKey getPublicKeyFromString(String publicKeyString) {
        Security.addProvider(new BouncyCastleProvider());

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory;
        try {
            keyFactory = KeyFactory.getInstance("ECDSA", "BC");
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException e) {
        }
        return null;
    }
    public static void main(String[] args) throws Exception {

    Asymmetric asymmetric = new Asymmetric();
    Keys keys = asymmetric.generateKeys();
    System.out.println(keys.publicKey);
    System.out.println(keys.privateKey);
    System.out.println(asymmetric.getPublicFromPrivateKey(keys.privateKey));
    String sign = asymmetric.sign("dd", keys.privateKey);
    System.out.println(sign);
    System.out.println(asymmetric.verify("dd", keys.publicKey,sign));





    }

}
