package com.leepandar.starter.storage.engine;

import com.leepandar.starter.storage.strategy.StorageStrategy;

import java.util.List;

/**
 * 存储策略注册
 * 主要针对配置文件
 */
public interface StorageStrategyRegistrar {

    /**
     * 注册策略到列表
     */
    void register(List<StorageStrategy> strategies);

}