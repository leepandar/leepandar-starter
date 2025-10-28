package com.leepandar.starter.datapermission.provider;

import com.leepandar.starter.datapermission.model.UserData;

/**
 * 数据权限用户数据提供者
 */
public interface DataPermissionUserDataProvider {

    /**
     * 是否过滤
     *
     * @return true：过滤；false：不过滤
     */
    boolean isFilter();

    /**
     * 获取用户数据
     *
     * @return 用户数据
     */
    UserData getUserData();
}
