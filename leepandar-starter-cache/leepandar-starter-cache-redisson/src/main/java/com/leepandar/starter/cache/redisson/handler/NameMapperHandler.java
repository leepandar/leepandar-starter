package com.leepandar.starter.cache.redisson.handler;

import cn.hutool.core.text.CharSequenceUtil;
import org.redisson.api.NameMapper;

/**
 * 缓存名称映射处理器
 */
public class NameMapperHandler implements NameMapper {

    private final String keyPrefix;

    public NameMapperHandler(String keyPrefix) {
        this.keyPrefix = keyPrefix;
    }

    @Override
    public String map(String name) {
        if (CharSequenceUtil.isNotBlank(name) && !name.startsWith(keyPrefix)) {
            return keyPrefix + name;
        }
        return name;
    }

    @Override
    public String unmap(String name) {
        if (CharSequenceUtil.isNotBlank(name) && name.startsWith(keyPrefix)) {
            return name.substring(keyPrefix.length());
        }
        return name;
    }
}
