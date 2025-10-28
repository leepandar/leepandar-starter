package com.leepandar.starter.log.model;

import java.time.Instant;

/**
 * 访问日志上下文
 */
public class AccessLogContext {

    /**
     * 开始时间
     */
    private final Instant startTime;

    /**
     * 结束时间
     */
    private Instant endTime;

    /**
     * 配置信息
     */
    private final LogProperties properties;

    private AccessLogContext(Builder builder) {
        this.startTime = builder.startTime;
        this.endTime = builder.endTime;
        this.properties = builder.properties;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public LogProperties getProperties() {
        return properties;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public static Builder builder() {
        return new Builder();
    }

    /**
     * 访问日志上下文构建者
     */
    public static class Builder {

        private Instant startTime;
        private Instant endTime;
        private LogProperties properties;

        private Builder() {
        }

        public Builder startTime(Instant startTime) {
            this.startTime = startTime;
            return this;
        }

        public Builder endTime(Instant endTime) {
            this.endTime = endTime;
            return this;
        }

        public Builder properties(LogProperties properties) {
            this.properties = properties;
            return this;
        }

        public AccessLogContext build() {
            return new AccessLogContext(this);
        }
    }
}
