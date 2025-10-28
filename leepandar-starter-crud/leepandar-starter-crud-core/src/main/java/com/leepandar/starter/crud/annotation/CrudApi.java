package com.leepandar.starter.crud.annotation;

import com.leepandar.starter.crud.enums.Api;

import java.lang.annotation.*;

/**
 * CRUD（增删改查）API
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CrudApi {

    /**
     * API 类型
     */
    Api value() default Api.LIST;
}
