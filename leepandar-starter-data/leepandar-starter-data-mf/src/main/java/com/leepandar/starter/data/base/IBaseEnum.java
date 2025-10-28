package com.leepandar.starter.data.base;

import java.io.Serializable;

/**
 * 枚举接口
 */
public interface IBaseEnum<T extends Serializable> {

    /**
     * 枚举描述
     *
     * @return 枚举描述
     */
    String getDescription();

    /**
     * 枚举数据库存储值
     */
    T getValue();

    /**
     * 颜色
     *
     * @return 颜色
     */
    default String getColor() {
        return null;
    }
}
