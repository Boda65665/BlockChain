package org.example.Cryptography;// Java program to create a
// asymmetric key

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.xml.bind.DatatypeConverter;
import java.security.KeyPair;
import java.security
        .KeyPairGenerator;
import java.security
        .SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.security.*;
import javax.crypto.*;


// Class to create an asymmetric key
public class Asymmetric {
    public record Keys(String publicKey, String privateKey) {
        public Keys(String publicKey, String privateKey) {
            this.publicKey = publicKey;
            this.privateKey = privateKey;
        }
    }

    public Keys generateKeys() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {


        Security.setProperty("crypto.policy", "unlimited");
        Security.addProvider(new BouncyCastleProvider());
        KeyPairGenerator generator = KeyPairGenerator.getInstance("EC", "BC");
        generator.initialize(new ECGenParameterSpec("prime192v1"), new SecureRandom());
        KeyPair keyPair = generator.generateKeyPair();

        // Convert keys to strings
        String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
        String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

        return new Keys(publicKeyString, privateKeyString);
    }
    public String sign(String message, String privateKeyString) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
            PrivateKey privateKey = KeyFactory.getInstance("EC").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
            signature.initSign(privateKey);
            signature.update(message.getBytes());
            byte[] signatureBytes = signature.sign();
            return Base64.getEncoder().encodeToString(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException | NoSuchProviderException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }


    public boolean verify(String message, String publicKeyString, String signatureString)  {
        try {
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            PublicKey publicKey = KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
            Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
            signature.initVerify(publicKey);
            signature.update(message.getBytes());
            return signature.verify(signatureBytes);
        }

        catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | InvalidKeyException | SignatureException e) {
            e.printStackTrace();
            return false;

        }
}


}


    // Generate key pair



