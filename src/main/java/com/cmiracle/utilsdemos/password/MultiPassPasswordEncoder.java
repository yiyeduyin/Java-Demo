package com.cmiracle.utilsdemos.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

public class MultiPassPasswordEncoder implements PasswordEncoder {
    private static final Logger logger = LoggerFactory.getLogger(MultiPassPasswordEncoder.class);

    private Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2a?\\$\\d\\d\\$[./0-9A-Za-z]{53}");
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
    private final SHAPasswordEncoder sha = new SHAPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return sha.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        if (encodedPassword == null || encodedPassword.length() == 0) {
            logger.warn("Empty encoded password");
            return false;
        }

        if (BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return bcrypt.matches(rawPassword, encodedPassword);
        } else {
            return sha.matches(rawPassword, encodedPassword);
        }
    }
}
