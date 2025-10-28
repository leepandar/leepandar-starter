package com.leepandar.starter.tenant.annotation;

import java.lang.annotation.*;

/**
 * 租户忽略注解
 * 例如：定时任务等需要忽略租户的场景
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TenantIgnore {
}
