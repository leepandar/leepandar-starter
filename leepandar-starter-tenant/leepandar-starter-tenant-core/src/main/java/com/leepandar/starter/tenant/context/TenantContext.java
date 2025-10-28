package com.leepandar.starter.tenant.context;


import com.leepandar.starter.tenant.config.TenantDataSource;
import com.leepandar.starter.tenant.enums.TenantIsolationLevel;

/**
 * 租户上下文
 */
public class TenantContext {

    /**
     * 租户 ID
     */
    private Long tenantId;

    /**
     * 隔离级别
     */
    private TenantIsolationLevel isolationLevel = TenantIsolationLevel.LINE;

    /**
     * 数据源信息
     */
    private TenantDataSource dataSource;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public TenantIsolationLevel getIsolationLevel() {
        return isolationLevel;
    }

    public void setIsolationLevel(TenantIsolationLevel isolationLevel) {
        this.isolationLevel = isolationLevel;
    }

    public TenantDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(TenantDataSource dataSource) {
        this.dataSource = dataSource;
    }
}
