package com.leepandar.starter.encrypt.encryptor;

import com.leepandar.starter.encrypt.context.CryptoContext;

/**
 * 加密器基类
 */
public abstract class AbstractEncryptor implements IEncryptor {

    protected AbstractEncryptor(CryptoContext context) {
        // 配置校验与配置注入
    }

}
