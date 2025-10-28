package com.leepandar.starter.message.mail.core;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import com.leepandar.starter.core.util.validation.ValidationUtils;

import java.util.Properties;

/**
 * 邮件配置
 *
 * @author Charles7c
 * @since 2.1.0
 */
public interface MailConfigurer {

    /**
     * 获取邮件配置
     *
     * @return 邮件配置
     */
    MailConfig getMailConfig();

    /**
     * 应用配置
     *
     * @param mailConfig 邮件配置
     * @param sender     邮件 Sender
     */
    default void apply(MailConfig mailConfig, JavaMailSenderImpl sender) {
        String protocolLowerCase = mailConfig.getProtocol().toLowerCase();
        ValidationUtils.throwIfNotEqual(MailConfig.DEFAULT_PROTOCOL, protocolLowerCase, "邮件配置不正确：不支持的邮件发送协议: %s"
            .formatted(mailConfig.getProtocol()));
        sender.setProtocol(mailConfig.getProtocol());

        ValidationUtils.throwIfBlank(mailConfig.getHost(), "邮件配置不正确：服务器地址不能为空");
        sender.setHost(mailConfig.getHost());

        ValidationUtils.throwIfNull(mailConfig.getPort(), "邮件配置不正确：服务器端口不能为空");
        sender.setPort(mailConfig.getPort());

        ValidationUtils.throwIfBlank(mailConfig.getUsername(), "邮件配置不正确：用户名不能为空");
        sender.setUsername(mailConfig.getUsername());

        ValidationUtils.throwIfBlank(mailConfig.getPassword(), "邮件配置不正确：密码不能为空");
        sender.setPassword(mailConfig.getPassword());

        if (mailConfig.getDefaultEncoding() != null) {
            sender.setDefaultEncoding(mailConfig.getDefaultEncoding().name());
        }

        Properties javaMailProperties = new Properties();
        if (!mailConfig.getProperties().isEmpty()) {
            javaMailProperties.putAll(mailConfig.getProperties());
            javaMailProperties.put("mail.from", mailConfig.getFrom());
        }
        javaMailProperties.put("mail.smtp.auth", true);
        if (mailConfig.isSslEnabled()) {
            ValidationUtils.throwIfNull(mailConfig.getSslPort(), "邮件配置不正确：SSL端口不能为空");
            javaMailProperties.put("mail.smtp.socketFactory.port", mailConfig.getSslPort());
            javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        }
        sender.setJavaMailProperties(javaMailProperties);
    }
}
