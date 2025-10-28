package com.leepandar.starter.tenant;

import com.leepandar.starter.tenant.config.TenantDataSource;

import javax.sql.DataSource;

/**
 * 租户数据源级隔离处理器
 */
public interface TenantDataSourceHandler {

    /**
     * 切换数据源
     *
     * @param tenantDataSource 数据源配置
     */
    void changeDataSource(TenantDataSource tenantDataSource);

    /**
     * 是否存在指定数据源
     *
     * @param dataSourceName 数据源名称
     * @return 是否存在指定数据源
     */
    boolean containsDataSource(String dataSourceName);

    /**
     * 创建数据源
     *
     * @param tenantDataSource 数据源配置
     * @return 数据源
     */
    DataSource createDataSource(TenantDataSource tenantDataSource);

    /**
     * 移除数据源
     *
     * @param dataSourceName 数据源名称
     */
    void removeDataSource(String dataSourceName);

    /**
     * 轮询数据源
     */
    void poll();
}
