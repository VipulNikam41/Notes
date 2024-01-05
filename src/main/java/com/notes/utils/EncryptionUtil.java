package com.notes.utils;

import lombok.experimental.UtilityClass;

import javax.crypto.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@UtilityClass
public class EncryptionUtil {
    private static final String ENCR_ALGO = "AES";
    private static SecretKey secretKey;
    private static Cipher cipherEncrypter;
    private static Cipher cipherDecrypter;

    static {
        try {
            secretKey = KeyGenerator.getInstance(ENCR_ALGO).generateKey();

            cipherEncrypter = Cipher.getInstance(ENCR_ALGO);
            cipherEncrypter.init(Cipher.ENCRYPT_MODE, secretKey);

            cipherDecrypter = Cipher.getInstance(ENCR_ALGO);
            cipherDecrypter.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException e) {
            // do nothing
        }
    }

    public static String encrypt(String plainText) {
        try {
            byte[] encryptedBytes = cipherEncrypter.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            // Encode the encrypted bytes as a Base64 string for easy storage or transmission
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }

    }

    public static String decrypt(String encryptedString) {
        // Decode the Base64 string to get the encrypted bytes
        byte[] encryptedBytes = Base64.getDecoder().decode(encryptedString);

        // Decrypt the bytes and convert to a string
        try {
            byte[] decryptedBytes = cipherDecrypter.doFinal(encryptedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            return null;
        }
    }
}
