package com.leepandar.starter.tenant.handler.line;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.leepandar.starter.tenant.autoconfigure.TenantProperties;
import com.leepandar.starter.tenant.context.TenantContextHolder;
import com.leepandar.starter.tenant.enums.TenantIsolationLevel;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认租户行级隔离处理器
 */
public class DefaultTenantLineHandler implements TenantLineHandler {

    private static final Logger log = LoggerFactory.getLogger(DefaultTenantLineHandler.class);
    private final TenantProperties tenantProperties;

    public DefaultTenantLineHandler(TenantProperties tenantProperties) {
        this.tenantProperties = tenantProperties;
    }

    @Override
    public Expression getTenantId() {
        Long tenantId = TenantContextHolder.getTenantId();
        if (tenantId != null) {
            return new LongValue(tenantId);
        }
        log.warn("Tenant ID not found in current context.");
        return new NullValue();
    }

    @Override
    public String getTenantIdColumn() {
        return tenantProperties.getTenantIdColumn();
    }

    @Override
    public boolean ignoreTable(String tableName) {
        // 忽略租户
        if (TenantContextHolder.isIgnore()) {
            return true;
        }
        // 忽略数据源级隔离
        if (TenantIsolationLevel.DATASOURCE.equals(TenantContextHolder.getIsolationLevel())) {
            return true;
        }
        // 忽略指定表
        return CollUtil.contains(tenantProperties.getIgnoreTables(), tableName);
    }
}