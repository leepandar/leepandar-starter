package com.leepandar.starter.tenant.interceptor;

import cn.hutool.core.annotation.AnnotationUtil;
import com.leepandar.starter.tenant.annotation.TenantIgnore;
import com.leepandar.starter.tenant.autoconfigure.TenantProperties;
import com.leepandar.starter.tenant.config.TenantProvider;
import com.leepandar.starter.tenant.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 租户拦截器
 */
public class TenantInterceptor implements HandlerInterceptor {

    private final TenantProperties tenantProperties;
    private final TenantProvider tenantProvider;

    public TenantInterceptor(TenantProperties tenantProperties, TenantProvider tenantProvider) {
        this.tenantProperties = tenantProperties;
        this.tenantProvider = tenantProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 设置上下文
        String tenantId = request.getHeader(tenantProperties.getTenantIdHeader());
        TenantContextHolder.setContext(tenantProvider.getByTenantId(tenantId, true));
        // 设置是否忽略租户
        TenantContextHolder.setIgnore(this.isIgnore(handler));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        // 清除上下文
        TenantContextHolder.clear();
    }

    /**
     * 是否忽略租户
     *
     * @param handler 处理器
     * @return 是否忽略租户
     */
    private boolean isIgnore(Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            TenantIgnore methodAnnotation = handlerMethod.getMethodAnnotation(TenantIgnore.class);
            if (methodAnnotation != null) {
                return true;
            }
            return AnnotationUtil.getAnnotation(handlerMethod.getBeanType(), TenantIgnore.class) != null;
        }
        return false;
    }
}