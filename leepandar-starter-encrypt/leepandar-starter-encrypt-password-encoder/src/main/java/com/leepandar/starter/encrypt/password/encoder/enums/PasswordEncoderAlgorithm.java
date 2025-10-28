package com.leepandar.starter.encrypt.password.encoder.enums;

import java.util.regex.Pattern;

/**
 * 密码编码器加密算法枚举
 */
public enum PasswordEncoderAlgorithm {

    /** BCrypt加密算法 */
    BCRYPT(Pattern.compile("\\A\\$2(a|y|b)?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}")),

    /** SCrypt加密算法 */
    SCRYPT(Pattern.compile("\\A\\$s0\\$[0-9a-f]+\\$[0-9a-f]+\\$[0-9a-f]+")),

    /** PBKDF2加密算法 */
    PBKDF2(Pattern.compile("\\A\\$pbkdf2-sha256\\$\\d+\\$[0-9a-f]+\\$[0-9a-f]+")),

    /** Argon2加密算法 */
    ARGON2(Pattern.compile("\\A\\$argon2(id|i|d)\\$v=\\d+\\$m=\\d+,t=\\d+,p=\\d+\\$[0-9a-zA-Z+/]+\\$[0-9a-zA-Z+/]+"));

    /** 正则匹配 */
    private final Pattern pattern;

    PasswordEncoderAlgorithm(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }
}
