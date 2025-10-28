package com.leepandar.starter.data.autoconfigure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.leepandar.starter.core.constant.PropertiesConstants;

import java.lang.annotation.*;

/**
 * 是否启用数据权限注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnProperty(prefix = "mybatis-flex.extension.data-permission", name = PropertiesConstants.ENABLED, havingValue = "true")
public @interface ConditionalOnEnabledDataPermission {
}