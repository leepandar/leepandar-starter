package com.leepandar.starter.storage.engine;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import com.leepandar.starter.storage.autoconfigure.properties.StorageProperties;
import com.leepandar.starter.storage.common.constant.StorageConstant;
import com.leepandar.starter.storage.common.enums.DefaultStorageSource;
import com.leepandar.starter.storage.common.exception.StorageException;
import com.leepandar.starter.storage.domain.model.resp.StrategyStatusResp;
import com.leepandar.starter.storage.strategy.StorageStrategy;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储策略路由器
 */
public class StorageStrategyRouter implements ApplicationListener<ApplicationEvent> {

    private static final Logger log = LoggerFactory.getLogger(StorageStrategyRouter.class);

    private final Map<String, StorageStrategy> configStrategies = new ConcurrentHashMap<>();
    private final Map<String, StorageStrategy> dynamicStrategies = new ConcurrentHashMap<>();
    private final Map<String, StorageStrategy> decoratedStrategies = new ConcurrentHashMap<>();

    private final StorageProperties storageProperties;
    private final String configDefaultPlatform;
    private volatile String dynamicDefaultPlatform;
    private final StorageDecoratorManager decoratorManager;

    public StorageStrategyRouter(List<StorageStrategyRegistrar> registrars,
                                 StorageProperties storageProperties,
                                 StorageDecoratorManager decoratorManager) {
        this.decoratorManager = decoratorManager;
        this.storageProperties = storageProperties;
        List<StorageStrategy> strategies = new ArrayList<>();
        for (StorageStrategyRegistrar registrar : registrars) {
            registrar.register(strategies);
        }

        // 配置文件加载的策略
        for (StorageStrategy strategy : strategies) {
            configStrategies.put(strategy.getPlatform(), strategy);
        }

        // 加载配置文件默认存储
        this.configDefaultPlatform = storageProperties.getDefaultPlatform();
    }

    /**
     * 存储选择（支持装饰器）
     */
    public StorageStrategy route(String platform) {
        // 1. 先检查缓存的装饰后策略
        StorageStrategy decorated = decoratedStrategies.get(platform);
        if (decorated != null) {
            return decorated;
        }
        // 2. 获取原始策略
        StorageStrategy strategy = getOriginalStrategy(platform);
        // 3. 应用装饰器
        StorageStrategy decoratedStrategy = applyDecoratorsIfAvailable(strategy);
        // 4. 缓存装饰后的策略
        decoratedStrategies.put(platform, decoratedStrategy);
        return decoratedStrategy;
    }

    /**
     * 获取原始策略
     */
    private StorageStrategy getOriginalStrategy(String platform) {
        return Optional.ofNullable(dynamicStrategies.get(platform))
            .or(() -> Optional.ofNullable(configStrategies.get(platform)))
            .orElseThrow(() -> new StorageException(String.format("不支持的存储平台: %s", platform)));
    }

    /**
     * 应用装饰器
     */
    private StorageStrategy applyDecoratorsIfAvailable(StorageStrategy strategy) {
        return ObjectUtil.isNotEmpty(decoratorManager) ? decoratorManager.applyDecorators(strategy) : strategy;
    }

    /**
     * 动态注册策略 - 支持装饰器注册
     */
    public void registerDynamic(StorageStrategy strategy) {
        String platform = strategy.getPlatform();
        if (dynamicStrategies.containsKey(platform)) {
            throw new StorageException("动态策略 platform 已存在: " + platform);
        }
        dynamicStrategies.put(platform, strategy);
        // 清除装饰器缓存，确保下次获取时重新应用装饰器
        decoratedStrategies.remove(platform);
    }

    /**
     * 卸载动态策略
     */
    public boolean unloadDynamic(String platform) {
        StorageStrategy strategy = dynamicStrategies.remove(platform);
        if (strategy == null) {
            return false;
        }
        decoratedStrategies.remove(platform);
        try {
            strategy.cleanup();
        } catch (Exception e) {
            log.error("清理存储策略失败: platform={}", platform, e);
        }
        return true;
    }

    /**
     * 监听装饰器变更事件，自动刷新缓存
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof StorageDecoratorManager.DecoratorRegisteredEvent || event instanceof StorageDecoratorManager.DecoratorUnregisteredEvent) {
            decoratedStrategies.clear();
        }
    }

    /**
     * 注册动态默认存储
     */
    public void registerDynamicDefaultStorage(String platform) {
        this.dynamicDefaultPlatform = platform;
    }

    /**
     * 获取默认存储平台
     */
    public String getDefaultStorage() {
        DefaultStorageSource defaultStorageSource = storageProperties.getDefaultStorageSource();
        return switch (defaultStorageSource) {
            case DYNAMIC -> {
                if (StrUtil.isBlank(dynamicDefaultPlatform)) {
                    throw new StorageException("动态默认存储平台配置为空");
                }
                yield dynamicDefaultPlatform;
            }
            case CONFIG -> {
                if (StrUtil.isBlank(configDefaultPlatform)) {
                    throw new StorageException("配置默认存储平台配置为空");
                }
                yield configDefaultPlatform;
            }
        };
    }

    /**
     * 获取所有可用平台
     */
    public Set<String> getAllPlatform() {
        Set<String> allPlatform = new HashSet<>(configStrategies.keySet());
        allPlatform.addAll(dynamicStrategies.keySet());
        return allPlatform;
    }

    /**
     * 检查是否为动态注册的策略
     */
    public boolean isDynamic(String platform) {
        return dynamicStrategies.containsKey(platform);
    }

    /**
     * 检查是否为配置文件策略
     */
    public boolean isFromConfig(String platform) {
        return configStrategies.containsKey(platform);
    }

    /**
     * 获取简化的策略信息
     */
    public Map<String, String> getActiveStrategyInfo() {
        Map<String, String> info = new HashMap<>();
        // 先添加配置文件策略
        configStrategies.keySet().forEach(platform -> info.put(platform, StorageConstant.CONFIG));
        // 动态策略会覆盖同名的配置策略
        dynamicStrategies.keySet().forEach(platform -> info.put(platform, StorageConstant.DYNAMIC));
        return info;
    }

    /**
     * 获取完整的策略状态
     */
    public Map<String, StrategyStatusResp> getFullStrategyStatus() {
        Map<String, StrategyStatusResp> status = new HashMap<>();

        // 所有唯一的 platform
        Set<String> appPlatform = new HashSet<>();
        appPlatform.addAll(configStrategies.keySet());
        appPlatform.addAll(dynamicStrategies.keySet());

        for (String platform : appPlatform) {
            boolean hasConfig = configStrategies.containsKey(platform);
            boolean hasDynamic = dynamicStrategies.containsKey(platform);
            boolean hasDecorated = decoratedStrategies.containsKey(platform);

            String activeType = hasDynamic ? StorageConstant.DYNAMIC : StorageConstant.CONFIG;
            String statusDesc = hasDynamic && hasConfig ? "配置策略被覆盖" : "正常";

            if (hasDecorated) {
                statusDesc += " (已装饰)";
            }

            StrategyStatusResp strategyStatusResp = new StrategyStatusResp(platform, hasConfig, hasDynamic, activeType, statusDesc);

            status.put(platform, strategyStatusResp);
        }

        return status;
    }
}