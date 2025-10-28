package com.leepandar.starter.web.annotation;

import com.leepandar.starter.web.autoconfigure.response.GlobalResponseAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 全局响应启用注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({GlobalResponseAutoConfiguration.class})
public @interface EnableGlobalResponse {}
