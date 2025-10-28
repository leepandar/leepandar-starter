package com.leepandar.starter.data.service.impl;

import com.leepandar.starter.core.util.ClassUtils;
import com.leepandar.starter.data.base.BaseMapper;
import com.leepandar.starter.data.service.IService;

/**
 * 通用业务实现类
 */
public class ServiceImpl<M extends BaseMapper<T>, T> extends com.mybatisflex.spring.service.impl.ServiceImpl<M, T> implements IService<T> {

    protected final Class<?>[] typeArguments = ClassUtils.getTypeArguments(this.getClass());
    protected final Class<T> entityClass = currentModelClass();

    protected Class<T> currentModelClass() {
        return (Class<T>)this.typeArguments[1];
    }

}