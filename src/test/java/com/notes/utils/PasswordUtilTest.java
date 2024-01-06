package com.notes.utils;

import com.notes.dto.CreateUser;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordUtilTest {
    @Test
    public void matchPasswordTest() {
        String password = "vipul";
        String passwordHash = PasswordUtil.hashPassword(password);

        assertTrue(
                PasswordUtil.matchPassword(password, passwordHash)
        );
        assertFalse(
                PasswordUtil.matchPassword("Vipul", passwordHash)
        );
        assertFalse(
                PasswordUtil.matchPassword("something", passwordHash)
        );
    }

}