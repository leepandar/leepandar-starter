package com.leepandar.starter.data.autoconfigure;

import com.mybatisflex.core.dialect.DbType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * MyBatis Plus 扩展配置属性
 */
@ConfigurationProperties("mybatis-flex.extension")
public class MyBatisFlexExtensionProperties {

    /**
     * 是否启用
     */
    private boolean enabled = false;

    /**
     * Mapper 接口扫描包（配置时必须使用：mapper-package 键名）
     * <p>
     * e.g. com.example.**.mapper
     * </p>
     */
    private String mapperPackage;

    /**
     * 数据权限插件配置
     */
    private DataPermissionProperties dataPermission;

    /**
     * 分页插件配置
     */
    private PaginationProperties pagination;

    /**
     * 数据权限插件配置属性
     */
    public static class DataPermissionProperties {

        /**
         * 是否启用
         */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }

    /**
     * 分页插件配置属性
     */
    public static class PaginationProperties {

        /**
         * 是否启用
         */
        private boolean enabled = true;

        /**
         * 数据库类型
         */
        private DbType dbType;

        /**
         * 是否溢出处理
         */
        private boolean overflow = false;

        /**
         * 单页分页条数限制（默认：-1 表示无限制）
         */
        private Long maxLimit = -1L;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public DbType getDbType() {
            return dbType;
        }

        public void setDbType(DbType dbType) {
            this.dbType = dbType;
        }

        public boolean isOverflow() {
            return overflow;
        }

        public void setOverflow(boolean overflow) {
            this.overflow = overflow;
        }

        public Long getMaxLimit() {
            return maxLimit;
        }

        public void setMaxLimit(Long maxLimit) {
            this.maxLimit = maxLimit;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMapperPackage() {
        return mapperPackage;
    }

    public void setMapperPackage(String mapperPackage) {
        this.mapperPackage = mapperPackage;
    }

    public DataPermissionProperties getDataPermission() {
        return dataPermission;
    }

    public void setDataPermission(DataPermissionProperties dataPermission) {
        this.dataPermission = dataPermission;
    }

    public PaginationProperties getPagination() {
        return pagination;
    }

    public void setPagination(PaginationProperties pagination) {
        this.pagination = pagination;
    }
}
