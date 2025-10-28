package com.leepandar.starter.core.util;

import cn.hutool.extra.spring.SpringUtil;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * Spring 工具类
 */
public class SpringUtils {

    private SpringUtils() {
    }

    /**
     * 获取代理对象
     *
     * @param target 目标对象
     * @param <T>    目标对象类型
     * @return 代理对象
     * @since 2.8.2
     */
    public static <T> T getProxy(T target) {
        return (T)SpringUtil.getBean(target.getClass());
    }

    /**
     * 通过 class 获取 Bean
     *
     * @param <T>                Bean类型
     * @param clazz              Bean类
     * @param ignoreNoSuchBeanEx 是否忽略 {@link NoSuchBeanDefinitionException}
     * @return Bean对象
     * @see SpringUtil#getBean(Class)
     * @since 2.13.1
     */
    public static <T> T getBean(Class<T> clazz, boolean ignoreNoSuchBeanEx) {
        try {
            return SpringUtil.getBean(clazz);
        } catch (NoSuchBeanDefinitionException e) {
            if (ignoreNoSuchBeanEx) {
                return null;
            }
            throw e;
        }
    }
}
