package de.flower.common.service.security;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

/**
 * @author flowerrrr
 */
@Service
public class PasswordGenerator implements IPasswordGenerator {

    // chars that might be hard to distinguish
    public static final String passwordChars = "23456789abcdefghkmnpqrstuvwxyz";

    @Override
    public String generatePassword() {
        String password = RandomStringUtils.random(4, passwordChars);
        return password;
    }
}
