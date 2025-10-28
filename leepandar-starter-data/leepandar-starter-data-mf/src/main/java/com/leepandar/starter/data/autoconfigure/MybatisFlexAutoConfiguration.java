package com.leepandar.starter.data.autoconfigure;

import com.mybatisflex.core.dialect.DbType;
import com.mybatisflex.core.dialect.DialectFactory;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.core.util.GeneralPropertySourceFactory;
import com.leepandar.starter.data.datapermission.DataPermissionDialect;
import com.leepandar.starter.data.datapermission.DataPermissionFilter;

/**
 * MyBatis Flex 自动配置
 */
@AutoConfiguration
@MapperScan("${mybatis-flex.extension.mapper-package}")
@EnableTransactionManagement(proxyTargetClass = true)
@EnableConfigurationProperties(MyBatisFlexExtensionProperties.class)
@ConditionalOnProperty(prefix = "mybatis-flex.extension", name = PropertiesConstants.ENABLED, havingValue = "true")
@PropertySource(value = "classpath:default-data-mybatis-flex.yml", factory = GeneralPropertySourceFactory.class)
public class MybatisFlexAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(MybatisFlexAutoConfiguration.class);

    @Resource
    private DataPermissionFilter dataPermissionFilter;

    @PostConstruct
    public void postConstruct() {
        log.debug("Auto Configuration 'MyBatis Flex' completed initialization.");
        DialectFactory.registerDialect(DbType.MYSQL, new DataPermissionDialect(dataPermissionFilter));
    }

}
