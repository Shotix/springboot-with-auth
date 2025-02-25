package com.timni.springbootwithauth.utils;

import org.apache.commons.codec.binary.Base64;
import java.security.SecureRandom;

/**
 * Utility class to generate a new jwt.secret and jwt.refreshSecret
 * The generated secrets should be copied to the application.properties file
 * IMPORTANT: Please make sure to keep the generated secrets secure and do not expose them to the public
 * <p> 
 * This class is not part of the application and should be removed after generating the secrets
 * @version 1.0
 */
public class JwtSecretGenerator {
    public static void main(String[] args) {
        System.out.println("New jwt.secret: " + generateSecret());
        System.out.println("New jwt.refreshSecret: " + generateSecret());
    }

    private static String generateSecret() {
        SecureRandom random = new SecureRandom();
        // Generate a 512-bit random key
        byte[] bytes = new byte[64];
        random.nextBytes(bytes);
        return Base64.encodeBase64String(bytes);
    }
}
