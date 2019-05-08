package com.cmiracle.utilsdemos.password;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;
import com.google.common.primitives.Bytes;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

public class SHAPasswordEncoder implements PasswordEncoder {
    private static final int SALT_LENGTH = 8;
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    @Override
    public String encode(CharSequence rawPassword) {
        byte[] salt = new byte[SALT_LENGTH];
        SECURE_RANDOM.nextBytes(salt);
        byte[] pwdBytes = rawPassword.toString().getBytes(Charsets.UTF_8);
        byte[] saltedHash = digest(salt, pwdBytes);
        return BaseEncoding.base16().encode(saltedHash);
    }

    private byte[] digest(byte[] salt, byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(Bytes.concat(salt, data));
            return Bytes.concat(salt, hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        byte[] expectedSaltedHash;
        try {
            expectedSaltedHash = BaseEncoding.base16().decode(encodedPassword);
        } catch (IllegalArgumentException e) {
            return false;
        }
        byte[] salt = Arrays.copyOfRange(expectedSaltedHash, 0, SALT_LENGTH);
        byte[] actualPwdBytes = rawPassword.toString().getBytes(Charsets.UTF_8);
        byte[] actualSaltedHash = digest(salt, actualPwdBytes);
        return Arrays.equals(expectedSaltedHash, actualSaltedHash);
    }
}
