package com.notes.utils;

import com.notes.dto.CreateUser;
import lombok.experimental.UtilityClass;
import org.mindrot.jbcrypt.BCrypt;

@UtilityClass
public class PasswordUtil {
    public static String hashPassword(String plainPassword) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(plainPassword, salt);
    }

    public static boolean matchPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    public static boolean confirmPassword(CreateUser user) {
        return user.getPassword().equals(user.getConfirmPassword());
    }
}
