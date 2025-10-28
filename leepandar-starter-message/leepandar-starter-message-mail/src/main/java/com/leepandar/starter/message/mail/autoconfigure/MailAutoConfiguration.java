package com.leepandar.starter.message.mail.autoconfigure;

import cn.hutool.core.map.MapUtil;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.leepandar.starter.core.util.GeneralPropertySourceFactory;
import com.leepandar.starter.core.util.MapUtils;

/**
 * 邮件自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(MailProperties.class)
@PropertySource(value = "classpath:default-messaging-mail.yml", factory = GeneralPropertySourceFactory.class)
public class MailAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MailAutoConfiguration.class);

    @Bean
    JavaMailSenderImpl mailSender(MailProperties properties) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        this.applyProperties(properties, sender);
        return sender;
    }

    /**
     * 应用邮件配置
     *
     * @param properties 邮件配置
     * @param sender     邮件 Sender
     */
    private void applyProperties(MailProperties properties, JavaMailSenderImpl sender) {
        sender.setHost(properties.getHost());
        if (properties.getPort() != null) {
            sender.setPort(properties.getPort());
        }

        sender.setUsername(properties.getUsername());
        sender.setPassword(properties.getPassword());
        sender.setProtocol(properties.getProtocol());
        if (properties.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(properties.getDefaultEncoding().name());
        }

        if (MapUtil.isNotEmpty(properties.getProperties())) {
            sender.setJavaMailProperties(MapUtils.toProperties(properties.getProperties()));
        }
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'Mail' completed initialization.");
    }
}
