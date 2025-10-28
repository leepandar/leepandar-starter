package com.leepandar.starter.data.datapermission;

import org.aspectj.lang.annotation.*;

@Aspect
public class DataPermissionAspect {

    // ThreadLocal用于存储注解信息
    private static final ThreadLocal<DataPermission> THREAD_LOCAL = new ThreadLocal<>();

    @Pointcut("@annotation(dataPermission)")
    public void dataPermissionPointcut(DataPermission dataPermission) {
    }

    @Before("dataPermissionPointcut(dataPermission)")
    public void beforeMethod(DataPermission dataPermission) {
        THREAD_LOCAL.set(dataPermission);
    }

    @AfterThrowing(pointcut = "dataPermissionPointcut(dataPermission)")
    public void afterThrowingMethod(DataPermission dataPermission) {
        THREAD_LOCAL.remove();
    }

    @After("dataPermissionPointcut(dataPermission)")
    public void afterMethod(DataPermission dataPermission) {
        THREAD_LOCAL.remove();
    }

    public static DataPermission currentDataPermission() {
        return THREAD_LOCAL.get();
    }

}
