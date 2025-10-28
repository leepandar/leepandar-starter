package com.leepandar.starter.encrypt.api.autoconfigure;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import com.leepandar.starter.core.constant.OrderedConstants;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.encrypt.api.filter.ApiEncryptFilter;

/**
 * API 加密自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(ApiEncryptProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.ENCRYPT_API, name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class ApiEncryptAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ApiEncryptAutoConfiguration.class);

    /**
     * API 加密过滤器
     */
    @Bean
    public FilterRegistrationBean<ApiEncryptFilter> apiEncryptFilter(ApiEncryptProperties properties) {
        FilterRegistrationBean<ApiEncryptFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new ApiEncryptFilter(properties));
        registrationBean.setOrder(OrderedConstants.Filter.API_ENCRYPT_FILTER);
        registrationBean.addUrlPatterns(StringConstants.PATH_PATTERN_CURRENT_DIR);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'Encrypt-API' completed initialization.");
    }
}
