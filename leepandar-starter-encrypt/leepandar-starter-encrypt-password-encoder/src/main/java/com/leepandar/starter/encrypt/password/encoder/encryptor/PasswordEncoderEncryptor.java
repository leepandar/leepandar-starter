package com.leepandar.starter.encrypt.password.encoder.encryptor;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.leepandar.starter.core.util.SpringUtils;
import com.leepandar.starter.encrypt.context.CryptoContext;
import com.leepandar.starter.encrypt.encryptor.AbstractEncryptor;
import com.leepandar.starter.encrypt.password.encoder.autoconfigure.PasswordEncoderProperties;

/**
 * 密码编码器加密器
 * 使用前必须注入 {@link PasswordEncoder}，此加密方式不可逆，适合于密码场景
 */
public class PasswordEncoderEncryptor extends AbstractEncryptor {

    private final PasswordEncoderProperties properties = SpringUtils.getBean(PasswordEncoderProperties.class, true);

    public PasswordEncoderEncryptor(CryptoContext context) {
        super(context);
    }

    @Override
    public String encrypt(String plaintext) {
        // 如果已经是加密格式，直接返回
        if (properties == null || properties.getAlgorithm().getPattern().matcher(plaintext).matches()) {
            return plaintext;
        }
        return SpringUtil.getBean(PasswordEncoder.class).encode(plaintext);
    }

    @Override
    public String decrypt(String ciphertext) {
        return ciphertext;
    }
}
