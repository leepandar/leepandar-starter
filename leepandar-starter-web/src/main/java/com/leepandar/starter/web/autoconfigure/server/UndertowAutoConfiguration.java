package com.leepandar.starter.web.autoconfigure.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

import io.undertow.Undertow;
import io.undertow.server.handlers.DisallowedMethodsHandler;
import io.undertow.util.HttpString;
import org.springframework.context.annotation.PropertySource;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.core.util.CollUtils;
import com.leepandar.starter.core.util.GeneralPropertySourceFactory;

/**
 * Undertow 自动配置
 */
@AutoConfiguration
@ConditionalOnWebApplication
@ConditionalOnClass(Undertow.class)
@EnableConfigurationProperties(ServerExtensionProperties.class)
@PropertySource(value = "classpath:default-server.yml", factory = GeneralPropertySourceFactory.class)
@ConditionalOnProperty(prefix = "server.extension", name = PropertiesConstants.ENABLED, havingValue = "true")
public class UndertowAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(UndertowAutoConfiguration.class);

    /**
     * Undertow 自定义配置
     */
    @Bean
    public WebServerFactoryCustomizer<UndertowServletWebServerFactory> customize(ServerExtensionProperties properties) {
        return factory -> {
            factory.addDeploymentInfoCustomizers(deploymentInfo -> deploymentInfo
                .addInitialHandlerChainWrapper(handler -> new DisallowedMethodsHandler(handler, CollUtils
                    .mapToSet(properties.getDisallowedMethods(), HttpString::tryFromString))));
            log.debug("Disallowed HTTP methods on Server Undertow: {}.", properties
                .getDisallowedMethods());
            log.debug("Auto Configuration 'Web-Server Undertow' completed initialization.");
        };
    }
}
