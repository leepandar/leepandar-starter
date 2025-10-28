package com.leepandar.starter.tenant.handler.datasource;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.leepandar.starter.tenant.TenantDataSourceHandler;
import com.leepandar.starter.tenant.context.TenantContextHolder;
import com.leepandar.starter.tenant.enums.TenantIsolationLevel;
import jakarta.annotation.Nonnull;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 租户数据源级隔离拦截器
 */
public class TenantDataSourceInterceptor implements MethodInterceptor {

    private final TenantDataSourceHandler tenantDataSourceHandler;

    public TenantDataSourceInterceptor(TenantDataSourceHandler tenantDataSourceHandler) {
        this.tenantDataSourceHandler = tenantDataSourceHandler;
    }

    @Override
    public Object invoke(@Nonnull MethodInvocation invocation) throws Throwable {
        // 忽略租户
        if (TenantContextHolder.isIgnore()) {
            return invocation.proceed();
        }
        // 忽略行级隔离
        if (TenantIsolationLevel.LINE.equals(TenantContextHolder.getIsolationLevel())) {
            return invocation.proceed();
        }
        // 切换数据源
        boolean isPush = false;
        try {
            tenantDataSourceHandler.changeDataSource(TenantContextHolder.getDataSource());
            isPush = true;
            return invocation.proceed();
        } finally {
            if (isPush) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }
}
