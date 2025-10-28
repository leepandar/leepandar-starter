package com.leepandar.starter.tenant.aop;

import com.leepandar.starter.tenant.annotation.TenantIgnore;
import com.leepandar.starter.tenant.context.TenantContextHolder;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * 租户忽略注解切面
 */
@Aspect
public class TenantIgnoreAspect {

    /**
     * 忽略租户
     *
     * @param joinPoint 切点
     * @return 返回结果
     * @throws Throwable 异常
     */
    @Around("@annotation(tenantIgnore)")
    public Object around(ProceedingJoinPoint joinPoint, TenantIgnore tenantIgnore) throws Throwable {
        boolean oldIgnore = TenantContextHolder.isIgnore();
        if (oldIgnore) {
            return joinPoint.proceed();
        }
        try {
            TenantContextHolder.setIgnore(true);
            return joinPoint.proceed();
        } finally {
            TenantContextHolder.setIgnore(false);
        }
    }
}
