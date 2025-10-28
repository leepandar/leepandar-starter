package com.leepandar.starter.storage.domain.model.resp;

/**
 * 存储策略状态
 */
public class StrategyStatusResp {

    /**
     * 平台
     */
    private String platform;

    /**
     * 是否有配置文件策略
     */
    private boolean hasConfig;
    /**
     * 是否有动态策略
     */
    private boolean hasDynamic;

    /**
     * 当前生效的类型："CONFIG" 或 "DYNAMIC"
     */
    private String activeType;

    /**
     * 描述
     */
    private String description;

    public StrategyStatusResp(String platform,
                              boolean hasConfig,
                              boolean hasDynamic,
                              String activeType,
                              String description) {
        this.platform = platform;
        this.hasConfig = hasConfig;
        this.hasDynamic = hasDynamic;
        this.activeType = activeType;
        this.description = description;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public boolean isHasConfig() {
        return hasConfig;
    }

    public void setHasConfig(boolean hasConfig) {
        this.hasConfig = hasConfig;
    }

    public boolean isHasDynamic() {
        return hasDynamic;
    }

    public void setHasDynamic(boolean hasDynamic) {
        this.hasDynamic = hasDynamic;
    }

    public String getActiveType() {
        return activeType;
    }

    public void setActiveType(String activeType) {
        this.activeType = activeType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
