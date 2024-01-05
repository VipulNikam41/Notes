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

    @Test
    public void confirmPassword() {
        CreateUser user = new CreateUser();

        String password = "1234";
        user.setPassword(password);
        user.setConfirmPassword(password);
        assertTrue(
                PasswordUtil.confirmPassword(user)
        );

        user.setConfirmPassword("password");
        assertFalse(
                PasswordUtil.confirmPassword(user)
        );

    }
}