package com.leepandar.starter.json.autoconfigure;

import java.math.BigInteger;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.*;
import com.fasterxml.jackson.datatype.jsr310.ser.*;

import cn.hutool.core.date.DatePattern;
import com.leepandar.starter.core.enums.BaseEnum;
import com.leepandar.starter.core.util.GeneralPropertySourceFactory;
import com.leepandar.starter.json.serializer.BaseEnumDeserializer;
import com.leepandar.starter.json.serializer.BaseEnumSerializer;
import com.leepandar.starter.json.serializer.BigNumberSerializer;
import com.leepandar.starter.json.serializer.SimpleDeserializersWrapper;

/**
 * Jackson 自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(JacksonExtensionProperties.class)
@PropertySource(value = "classpath:default-json-jackson.yml", factory = GeneralPropertySourceFactory.class)
public class JacksonAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JacksonAutoConfiguration.class);
    private final JacksonExtensionProperties properties;

    public JacksonAutoConfiguration(JacksonExtensionProperties properties) {
        this.properties = properties;
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            JavaTimeModule javaTimeModule = this.javaTimeModule();
            SimpleModule baseEnumModule = this.baseEnumModule();
            SimpleModule bigNumberModule = this.bigNumberModule();

            builder.timeZone(TimeZone.getDefault());
            builder.modules(javaTimeModule, baseEnumModule, bigNumberModule);
            log.debug("Auto Configuration 'Jackson' completed initialization.");
        };
    }

    /**
     * 日期时间序列化及反序列化配置
     *
     * @return {@link JavaTimeModule}
     * @since 1.0.0
     */
    private JavaTimeModule javaTimeModule() {
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        // 针对时间类型：LocalDateTime 的序列化和反序列化处理
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN);
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));
        // 针对时间类型：LocalDate 的序列化和反序列化处理
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_DATE_PATTERN);
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        // 针对时间类型：LocalTime 的序列化和反序列化处理
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(DatePattern.NORM_TIME_PATTERN);
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));
        // 针对时间类型：Instant 的序列化和反序列化处理
        javaTimeModule.addSerializer(Instant.class, InstantSerializer.INSTANCE);
        javaTimeModule.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
        // 针对时间类型：Duration 的序列化和反序列化处理
        javaTimeModule.addSerializer(Duration.class, DurationSerializer.INSTANCE);
        javaTimeModule.addDeserializer(Duration.class, DurationDeserializer.INSTANCE);
        return javaTimeModule;
    }

    /**
     * 枚举序列化及反序列化配置
     *
     * @return SimpleModule /
     * @since 2.4.0
     */
    private SimpleModule baseEnumModule() {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addSerializer(BaseEnum.class, BaseEnumSerializer.SERIALIZER_INSTANCE);
        SimpleDeserializersWrapper deserializers = new SimpleDeserializersWrapper();
        deserializers.addDeserializer(BaseEnum.class, BaseEnumDeserializer.SERIALIZER_INSTANCE);
        simpleModule.setDeserializers(deserializers);
        return simpleModule;
    }

    /**
     * 大数值序列化及反序列化配置
     *
     * @return SimpleModule /
     * @since 2.12.1
     */
    private SimpleModule bigNumberModule() {
        SimpleModule bigNumberModule = new SimpleModule();
        switch (properties.getBigNumberSerializeMode()) {
            case FLEXIBLE -> {
                bigNumberModule.addSerializer(Long.class, BigNumberSerializer.SERIALIZER_INSTANCE);
                bigNumberModule.addSerializer(Long.TYPE, BigNumberSerializer.SERIALIZER_INSTANCE);
                bigNumberModule.addSerializer(BigInteger.class, BigNumberSerializer.SERIALIZER_INSTANCE);
            }
            case TO_STRING -> {
                bigNumberModule.addSerializer(Long.class, ToStringSerializer.instance);
                bigNumberModule.addSerializer(Long.TYPE, ToStringSerializer.instance);
                bigNumberModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
            }
            default -> log.warn("Jackson 大数值序列化模式：NO_OPERATE，超过 JS 范围的数值会损失精度");
        }
        return bigNumberModule;
    }
}
