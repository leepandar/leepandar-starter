package com.leepandar.starter.tenant.context;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import com.leepandar.starter.core.util.SpringUtils;
import com.leepandar.starter.tenant.autoconfigure.TenantProperties;
import com.leepandar.starter.tenant.config.TenantDataSource;
import com.leepandar.starter.tenant.enums.TenantIsolationLevel;

import java.util.Optional;

/**
 * 租户上下文 Holder
 */
public class TenantContextHolder {

    /**
     * 租户上下文
     */
    private static final TransmittableThreadLocal<TenantContext> CONTEXT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 是否忽略租户
     */
    private static final TransmittableThreadLocal<Boolean> IGNORE_HOLDER = new TransmittableThreadLocal<>();

    private TenantContextHolder() {
    }

    /**
     * 设置上下文
     *
     * @param context 上下文
     */
    public static void setContext(TenantContext context) {
        CONTEXT_HOLDER.set(context);
    }

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    public static TenantContext getContext() {
        return CONTEXT_HOLDER.get();
    }

    /**
     * 设置是否忽略租户
     *
     * @param ignore 是否忽略租户
     */
    public static void setIgnore(boolean ignore) {
        IGNORE_HOLDER.set(ignore);
    }

    /**
     * 是否忽略租户
     *
     * @return 是否忽略租户
     */
    public static boolean isIgnore() {
        return Boolean.TRUE.equals(IGNORE_HOLDER.get());
    }

    /**
     * 清除
     */
    public static void clear() {
        CONTEXT_HOLDER.remove();
        IGNORE_HOLDER.remove();
    }

    /**
     * 获取租户 ID
     *
     * @return 租户 ID
     */
    public static Long getTenantId() {
        return Optional.ofNullable(getContext()).map(TenantContext::getTenantId).orElse(null);
    }

    /**
     * 获取租户隔离级别
     *
     * @return 租户隔离级别
     */
    public static TenantIsolationLevel getIsolationLevel() {
        return Optional.ofNullable(getContext())
            .map(TenantContext::getIsolationLevel)
            .orElse(SpringUtil.getBean(TenantProperties.class).getIsolationLevel());
    }

    /**
     * 获取租户数据源
     *
     * @return 租户数据源
     */
    public static TenantDataSource getDataSource() {
        return Optional.ofNullable(getContext()).map(TenantContext::getDataSource).orElse(null);
    }

    /**
     * 是否启用了租户
     *
     * @return 是否启用了租户
     */
    public static boolean isTenantEnabled() {
        TenantProperties tenantProperties = SpringUtils.getBean(TenantProperties.class, true);
        return tenantProperties != null && tenantProperties.isEnabled();
    }

    /**
     * 是否禁用了租户
     *
     * @return 是否禁用了租户
     */
    public static boolean isTenantDisabled() {
        return !isTenantEnabled();
    }
}
