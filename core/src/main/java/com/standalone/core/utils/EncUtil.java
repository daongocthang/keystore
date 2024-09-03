package com.standalone.core.utils;

import android.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class EncUtil {

    public static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean check(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    public static String encrypt(String plainText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, secret);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        byte[] iv = cipher.getIV();

        byte[] encryptedBytesWithIV = new byte[encryptedBytes.length + iv.length];
        System.arraycopy(iv, 0, encryptedBytesWithIV, 0, iv.length);
        System.arraycopy(encryptedBytes, 0, encryptedBytesWithIV, iv.length, encryptedBytes.length);

        return Base64.encodeToString(encryptedBytesWithIV, Base64.DEFAULT);
    }


    public static String decrypt(String cipherText, SecretKey secret) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        byte[] encryptedBytesWithIV = Base64.decode(cipherText, Base64.DEFAULT);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        byte[] iv = Arrays.copyOfRange(encryptedBytesWithIV, 0, cipher.getBlockSize());
        byte[] encryptedBytes = Arrays.copyOfRange(encryptedBytesWithIV, cipher.getBlockSize(), encryptedBytesWithIV.length);
        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(iv));
        return new String(cipher.doFinal(encryptedBytes), StandardCharsets.UTF_8);
    }

    public static SecretKey genAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        keygen.init(256);
        return keygen.generateKey();
    }

    public static SecretKey genAESKey(char[] password) throws NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] salt = new byte[100];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        // iterationCount = 65536 (min = 1000)
        // keyLength = 256 (which can be 128, 196, or 256 for AES)
        KeySpec keySpec = new PBEKeySpec(password, salt, 65536, 256);
        return new SecretKeySpec(factory.generateSecret(keySpec).getEncoded(), "AES");
    }
}
