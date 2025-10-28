package com.leepandar.starter.data.base;

import cn.hutool.core.util.ClassUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.row.Db;

import java.util.Collection;

/**
 * Mapper 基类
 */
public interface BaseMapper<T> extends com.mybatisflex.core.BaseMapper<T> {

    /**
     * 批量更新记录
     *
     * @param entityList 实体列表
     * @return 是否成功
     */
    default boolean updateBatchById(Collection<T> entityList) {
        return Db.updateEntitiesBatch(entityList) > 0;
    }

    /**
     * 链式查询
     *
     * @return QueryWrapper 的包装类
     */
    default QueryWrapper query() {
        return QueryWrapper.create();
    }

    /**
     * 链式查询
     *
     * @return QueryWrapper 的包装类
     */
    default QueryWrapper query(T entity) {
        return QueryWrapper.create(entity);
    }

    /**
     * 获取实体类 Class 对象
     *
     * @return 实体类 Class 对象
     */
    default Class<T> currentEntityClass() {
        return (Class<T>)ClassUtil.getTypeArgument(this.getClass(), 0);
    }

}
