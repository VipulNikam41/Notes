package com.notes.utils;

import org.junit.Test;

import static org.junit.Assert.*;

public class EncryptionUtilTest {

    @Test
    public void encryptionDecryptionTest() {
        String plainText = "vipul";
        String encryptedString = EncryptionUtil.encrypt(plainText);

        assertEquals(plainText, EncryptionUtil.decrypt(encryptedString));
    }
}