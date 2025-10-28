package com.leepandar.starter.idempotent.generator;

import java.lang.reflect.Method;

/**
 * 幂等名称生成器
 */
public interface IdempotentNameGenerator {

    /**
     * 生成幂等名称
     *
     * @param target 目标实例
     * @param method 目标方法
     * @param args   方法参数
     * @return 幂等名称
     */
    String generate(Object target, Method method, Object... args);
}