package com.leepandar.starter.ratelimiter.autoconfigure;

import jakarta.annotation.PostConstruct;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import com.leepandar.starter.cache.redisson.autoconfigure.RedissonAutoConfiguration;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.ratelimiter.aop.RateLimiterAspect;
import com.leepandar.starter.ratelimiter.generator.DefaultRateLimiterNameGenerator;
import com.leepandar.starter.ratelimiter.generator.RateLimiterNameGenerator;

/**
 * 限流器自动配置
 */
@AutoConfiguration(after = RedissonAutoConfiguration.class)
@EnableConfigurationProperties(RateLimiterProperties.class)
@ConditionalOnProperty(prefix = PropertiesConstants.RATE_LIMITER, name = PropertiesConstants.ENABLED, havingValue = "true", matchIfMissing = true)
public class RateLimiterAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterAutoConfiguration.class);

    /**
     * 限流器切面
     */
    @Bean
    public RateLimiterAspect rateLimiterAspect(RateLimiterProperties properties,
                                               RateLimiterNameGenerator rateLimiterNameGenerator,
                                               RedissonClient redissonClient) {
        return new RateLimiterAspect(properties, rateLimiterNameGenerator, redissonClient);
    }

    /**
     * 限流器名称生成器
     */
    @Bean
    @ConditionalOnMissingBean
    public RateLimiterNameGenerator nameGenerator() {
        return new DefaultRateLimiterNameGenerator();
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'RateLimiter' completed initialization.");
    }
}
