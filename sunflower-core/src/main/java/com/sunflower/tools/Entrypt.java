package com.sunflower.tools;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author sunflower
 */
public final class Entrypt {
    private static final String HASH_ALGORITHM = "SHA-1";
    private static final int HASH_INTERATIONS = 1024;

    private Entrypt() {
    }

    public static String entryptPassword(String plainPassword) {
        try {
            byte[] salt = new byte[8];
            (new SecureRandom()).nextBytes(salt);
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(salt);
            byte[] result = digest.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            for(int i = 1; i < HASH_INTERATIONS; ++i) {
                digest.reset();
                result = digest.digest(result);
            }

            return new String(Hex.encodeHex(salt)) + new String(Hex.encodeHex(result));
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("Auto-generated method stub", e);
        }
    }

    public static boolean validatePassword(String plainPassword, String password) {
        try {
            byte[] salt = Hex.decodeHex(password.substring(0, 16).toCharArray());
            byte[] hashPassword = digest(plainPassword.getBytes(StandardCharsets.UTF_8), HASH_ALGORITHM, salt, 1024);
            return password.equals(Hex.encodeHexString(salt) + Hex.encodeHexString(hashPassword));
        } catch (NoSuchAlgorithmException | DecoderException e) {
            throw new UnsupportedOperationException("Auto-generated method stub", e);
        }
    }

    private static byte[] digest(byte[] input, String algorithm, byte[] salt, int iterations) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(algorithm);
        if (salt != null) {
            digest.update(salt);
        }

        byte[] result = digest.digest(input);

        for(int i = 1; i < iterations; ++i) {
            digest.reset();
            result = digest.digest(result);
        }

        return result;
    }
}
