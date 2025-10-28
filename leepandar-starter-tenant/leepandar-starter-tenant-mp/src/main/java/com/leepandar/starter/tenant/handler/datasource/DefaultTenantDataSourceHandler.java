package com.leepandar.starter.tenant.handler.datasource;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.leepandar.starter.tenant.TenantDataSourceHandler;
import com.leepandar.starter.tenant.config.TenantDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;

/**
 * 默认租户数据源级隔离处理器
 */
public class DefaultTenantDataSourceHandler implements TenantDataSourceHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultTenantDataSourceHandler.class);
    private final DynamicRoutingDataSource dynamicRoutingDataSource;
    private final DefaultDataSourceCreator dataSourceCreator;

    public DefaultTenantDataSourceHandler(DataSource dataSource) {
        this.dynamicRoutingDataSource = (DynamicRoutingDataSource)dataSource;
        this.dataSourceCreator = SpringUtil.getBean(DefaultDataSourceCreator.class);
    }

    @Override
    public void changeDataSource(TenantDataSource tenantDataSource) {
        if (tenantDataSource == null) {
            return;
        }
        String dataSourceName = tenantDataSource.getPoolName();
        if (!this.containsDataSource(dataSourceName)) {
            DataSource datasource = this.createDataSource(tenantDataSource);
            dynamicRoutingDataSource.addDataSource(dataSourceName, datasource);
            log.info("Load data source: {}", dataSourceName);
        }
        DynamicDataSourceContextHolder.push(dataSourceName);
        log.info("Change data source: {}", dataSourceName);
    }

    @Override
    public boolean containsDataSource(String dataSourceName) {
        return CharSequenceUtil.isNotBlank(dataSourceName) && dynamicRoutingDataSource.getDataSources()
            .containsKey(dataSourceName);
    }

    @Override
    public DataSource createDataSource(TenantDataSource tenantDataSource) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        dataSourceProperty.setPoolName(tenantDataSource.getPoolName());
        dataSourceProperty.setDriverClassName(tenantDataSource.getDriverClassName());
        dataSourceProperty.setUrl(tenantDataSource.getUrl());
        dataSourceProperty.setUsername(tenantDataSource.getUsername());
        dataSourceProperty.setPassword(tenantDataSource.getPassword());
        return dataSourceCreator.createDataSource(dataSourceProperty);
    }

    @Override
    public void removeDataSource(String dataSourceName) {
        dynamicRoutingDataSource.removeDataSource(dataSourceName);
    }

    @Override
    public void poll() {
        DynamicDataSourceContextHolder.poll();
    }
}