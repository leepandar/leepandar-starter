package com.leepandar.starter.crud.aop;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.BridgeMethodResolver;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ClassUtils;
import com.leepandar.starter.crud.annotation.CrudApi;
import com.leepandar.starter.crud.controller.AbstractCrudController;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * CRUD API 注解拦截器
 */
public class CrudApiAnnotationInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        // 获取目标类
        Class<?> targetClass = AopUtils.getTargetClass(Objects.requireNonNull(invocation.getThis()));
        // 获取目标方法
        Method specificMethod = ClassUtils.getMostSpecificMethod(invocation.getMethod(), targetClass);
        Method targetMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
        // 获取 @CrudApi 注解
        CrudApi crudApi = AnnotatedElementUtils.findMergedAnnotation(targetMethod, CrudApi.class);
        // 执行处理
        AbstractCrudController crudController = (AbstractCrudController)invocation.getThis();
        crudController.preHandle(crudApi, invocation.getArguments(), targetMethod, targetClass);
        return invocation.proceed();
    }
}
