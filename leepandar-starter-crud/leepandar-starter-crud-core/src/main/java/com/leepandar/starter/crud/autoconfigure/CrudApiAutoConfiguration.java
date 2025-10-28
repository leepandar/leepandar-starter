package com.leepandar.starter.crud.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.leepandar.starter.crud.annotation.CrudApi;
import com.leepandar.starter.crud.aop.CrudApiAnnotationAdvisor;
import com.leepandar.starter.crud.aop.CrudApiAnnotationInterceptor;

/**
 * CRUD API 自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(CrudProperties.class)
public class CrudApiAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(CrudApiAutoConfiguration.class);

    /**
     * CRUD API 注解通知
     */
    @Bean
    @ConditionalOnMissingBean
    public CrudApiAnnotationAdvisor crudApiAnnotationAdvisor(CrudApiAnnotationInterceptor crudApiAnnotationInterceptor) {
        return new CrudApiAnnotationAdvisor(crudApiAnnotationInterceptor, CrudApi.class);
    }

    /**
     * CRUD API 注解拦截器
     */
    @Bean
    @ConditionalOnMissingBean
    public CrudApiAnnotationInterceptor crudApiAnnotationInterceptor() {
        return new CrudApiAnnotationInterceptor();
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'CRUD API' completed initialization.");
    }
}
