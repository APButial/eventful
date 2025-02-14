package com.btp.login.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

// Uses SHA-512 for hashing password
public class PassHashService {
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return  hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error in hashing service", e);
        }
    }
}
