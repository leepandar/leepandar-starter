package com.leepandar.starter.tenant.autoconfigure;

import com.leepandar.starter.core.constant.OrderedConstants;
import com.leepandar.starter.tenant.annotation.ConditionalOnEnabledTenant;
import com.leepandar.starter.tenant.config.TenantProvider;
import com.leepandar.starter.tenant.interceptor.TenantInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 租户 Web MVC 自动配置
 */
@AutoConfiguration
@ConditionalOnEnabledTenant
@ConditionalOnWebApplication
@EnableConfigurationProperties(TenantProperties.class)
public class TenantWebMvcAutoConfiguration implements WebMvcConfigurer {

    private final TenantProperties tenantProperties;
    private final TenantProvider tenantProvider;

    public TenantWebMvcAutoConfiguration(TenantProperties tenantProperties, TenantProvider tenantProvider) {
        this.tenantProperties = tenantProperties;
        this.tenantProvider = tenantProvider;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TenantInterceptor(tenantProperties, tenantProvider))
            .order(OrderedConstants.Interceptor.TENANT_INTERCEPTOR);
    }
}
