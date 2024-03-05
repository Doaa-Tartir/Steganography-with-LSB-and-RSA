import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.*;
import javax.imageio.ImageIO;

public class rsaAlgorithm {

    KeyPair keyPair;
    //// 1
    public void keyGeneration(int bitLength) {
        // Generate rsa key pair
        KeyPairGenerator rsaKeyPairGenerator =null;

        try {
            //use RSA algorithm
            rsaKeyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //to create keys
        rsaKeyPairGenerator.initialize(bitLength);
        keyPair = rsaKeyPairGenerator.generateKeyPair();
    }

    ////2
    // Encrypt secret message with rsaAlgorithm public key
    public byte[] encryption(String secretMessage) throws InvalidKeyException, InvalidKeySpecException,
            NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
        //array of bytes because RSA key is in binary.
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        //generate publicKey object from KeyFactory class
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
        Cipher rsaCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        //initialize for encryption
        rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedSecretMessage = rsaCipher.doFinal(secretMessage.getBytes());
        return encryptedSecretMessage;
    }


    /////5
    // Decrypt secret message with rsaAlgorithm private key
    public String decryption(byte [] encryptedSecretMessageLoadedLSB) throws InvalidKeySpecException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
        Cipher rsaCipher1 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        rsaCipher1.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decryptedSecretMessageLSB = rsaCipher1.doFinal(encryptedSecretMessageLoadedLSB);
        System.out.println("Decrypted secret message: " + new String(decryptedSecretMessageLSB));
        return new String(decryptedSecretMessageLSB);
    }
}
