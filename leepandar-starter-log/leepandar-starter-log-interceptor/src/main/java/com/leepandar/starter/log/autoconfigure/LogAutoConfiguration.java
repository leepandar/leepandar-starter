package com.leepandar.starter.log.autoconfigure;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.DispatcherType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.leepandar.starter.core.constant.OrderedConstants;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.log.annotation.ConditionalOnEnabledLog;
import com.leepandar.starter.log.dao.LogDao;
import com.leepandar.starter.log.dao.impl.DefaultLogDaoImpl;
import com.leepandar.starter.log.handler.InterceptorLogHandler;
import com.leepandar.starter.log.filter.LogFilter;
import com.leepandar.starter.log.handler.LogHandler;
import com.leepandar.starter.log.interceptor.LogInterceptor;
import com.leepandar.starter.log.model.LogProperties;

/**
 * 日志自动配置
 */
@Configuration
@ConditionalOnEnabledLog
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class LogAutoConfiguration implements WebMvcConfigurer {

    private static final Logger log = LoggerFactory.getLogger(LogAutoConfiguration.class);
    private final LogProperties logProperties;

    public LogAutoConfiguration(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor(logProperties, logHandler(), logDao()))
            .addPathPatterns(StringConstants.PATH_PATTERN)
            .excludePathPatterns(logProperties.getExcludePatterns())
            .order(OrderedConstants.Interceptor.LOG_INTERCEPTOR);
    }

    /**
     * 日志过滤器
     */
    @Bean
    @ConditionalOnMissingBean
    public FilterRegistrationBean<LogFilter> logFilter() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new LogFilter(logProperties));
        registrationBean.setOrder(OrderedConstants.Filter.LOG_FILTER);
        registrationBean.addUrlPatterns(StringConstants.PATH_PATTERN_CURRENT_DIR);
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        return registrationBean;
    }

    /**
     * 日志处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public LogHandler logHandler() {
        return new InterceptorLogHandler();
    }

    /**
     * 日志持久层接口
     */
    @Bean
    @ConditionalOnMissingBean
    public LogDao logDao() {
        return new DefaultLogDaoImpl();
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'Log-Interceptor' completed initialization.");
    }
}
