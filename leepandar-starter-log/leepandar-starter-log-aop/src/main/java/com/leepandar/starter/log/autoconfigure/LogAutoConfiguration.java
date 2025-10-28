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
import com.leepandar.starter.core.constant.OrderedConstants;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.log.annotation.ConditionalOnEnabledLog;
import com.leepandar.starter.log.aspect.AccessLogAspect;
import com.leepandar.starter.log.aspect.LogAspect;
import com.leepandar.starter.log.dao.LogDao;
import com.leepandar.starter.log.dao.impl.DefaultLogDaoImpl;
import com.leepandar.starter.log.filter.LogFilter;
import com.leepandar.starter.log.handler.AopLogHandler;
import com.leepandar.starter.log.handler.LogHandler;
import com.leepandar.starter.log.model.LogProperties;

/**
 * 日志自动配置
 */
@Configuration
@ConditionalOnEnabledLog
@EnableConfigurationProperties(LogProperties.class)
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
public class LogAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LogAutoConfiguration.class);
    private final LogProperties logProperties;
    private final LogHandler logHandler;

    public LogAutoConfiguration(LogProperties logProperties, LogHandler logHandler) {
        this.logProperties = logProperties;
        this.logHandler = logHandler;
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
     * 日志切面
     *
     * @param logDao 日志持久层接口
     * @return {@link LogAspect }
     */
    @Bean
    @ConditionalOnMissingBean
    public LogAspect logAspect(LogDao logDao) {
        return new LogAspect(logProperties, logHandler, logDao);
    }

    /**
     * 访问日志切面
     *
     * @return {@link LogAspect }
     */
    @Bean
    @ConditionalOnMissingBean
    public AccessLogAspect accessLogAspect() {
        return new AccessLogAspect(logProperties, logHandler);
    }

    /**
     * 日志处理器
     */
    @Bean
    @ConditionalOnMissingBean
    public LogHandler logHandler() {
        return new AopLogHandler();
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
        log.debug("Auto Configuration 'Log-AOP' completed initialization.");
    }
}
