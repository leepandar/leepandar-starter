package com.leepandar.starter.crud.annotation;

import org.springframework.context.annotation.Import;
import com.leepandar.starter.crud.autoconfigure.CrudApiAutoConfiguration;
import com.leepandar.starter.crud.autoconfigure.CrudRequestMappingAutoConfiguration;

import java.lang.annotation.*;

/**
 * CRUD API 启用注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({CrudRequestMappingAutoConfiguration.class, CrudApiAutoConfiguration.class})
public @interface EnableCrudApi {}
