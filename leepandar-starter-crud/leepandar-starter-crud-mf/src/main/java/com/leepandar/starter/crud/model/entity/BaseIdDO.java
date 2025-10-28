package com.leepandar.starter.crud.model.entity;

import com.mybatisflex.annotation.Id;

import java.io.Serial;
import java.io.Serializable;

/**
 * 实体类基类
 * 通用字段：ID 主键
 */
public class BaseIdDO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    @Id
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
