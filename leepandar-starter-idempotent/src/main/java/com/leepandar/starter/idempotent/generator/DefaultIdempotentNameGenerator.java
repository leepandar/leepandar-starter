package com.leepandar.starter.idempotent.generator;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import com.leepandar.starter.core.constant.StringConstants;

import java.lang.reflect.Method;

/**
 * 默认幂等名称生成器
 */
public class DefaultIdempotentNameGenerator implements IdempotentNameGenerator {

    @Override
    public String generate(Object target, Method method, Object... args) {
        StringBuilder nameSb = new StringBuilder();
        // 添加类名
        nameSb.append(ClassUtil.getClassName(target, false));
        nameSb.append(StringConstants.COLON);
        // 添加方法名
        nameSb.append(method.getName());
        // 添加参数信息的哈希值（如果有参数）
        if (args != null && args.length > 0) {
            nameSb.append(StringConstants.COLON);
            // 使用JSONUtil序列化参数，然后计算哈希值以确保唯一性
            String argsJson = JSONUtil.toJsonStr(args);
            nameSb.append(DigestUtil.md5Hex(argsJson));
        }
        return nameSb.toString();
    }
}
