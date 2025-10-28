package com.leepandar.starter.log.annotation;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.leepandar.starter.core.constant.PropertiesConstants;

import java.lang.annotation.*;

/**
 * 是否启用日志记录注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@ConditionalOnProperty(prefix = PropertiesConstants.LOG, name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnEnabledLog {
}