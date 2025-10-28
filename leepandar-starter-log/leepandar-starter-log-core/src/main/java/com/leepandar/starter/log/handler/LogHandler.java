package com.leepandar.starter.log.handler;

import com.leepandar.starter.log.enums.Include;
import com.leepandar.starter.log.model.AccessLogContext;
import com.leepandar.starter.log.model.LogRecord;

import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Set;

/**
 * 日志处理器
 */
public interface LogHandler {

    /**
     * 是否记录日志
     *
     * @param targetMethod 目标方法
     * @param targetClass  目标类
     * @return 是否记录日志
     */
    boolean isRecord(Method targetMethod, Class<?> targetClass);

    /**
     * 开始日志记录
     *
     * @param startTime 开始时间
     * @return 日志记录器
     */
    LogRecord.Started start(Instant startTime);

    /**
     * 结束日志记录
     *
     * @param started  开始日志记录器
     * @param endTime  结束时间
     * @param includes 包含信息
     * @return 日志记录
     */
    LogRecord finish(LogRecord.Started started, Instant endTime, Set<Include> includes);

    /**
     * 结束日志记录
     *
     * @param started      开始日志记录器-
     * @param endTime      结束时间
     * @param includes     包含信息
     * @param targetMethod 目标方法
     * @param targetClass  目标类
     * @return 日志记录
     */
    LogRecord finish(LogRecord.Started started,
                     Instant endTime,
                     Set<Include> includes,
                     Method targetMethod,
                     Class<?> targetClass);

    /**
     * 记录日志描述
     *
     * @param logRecord    日志记录
     * @param targetMethod 目标方法
     */
    void logDescription(LogRecord logRecord, Method targetMethod);

    /**
     * 记录所属模块
     *
     * @param logRecord    日志记录
     * @param targetMethod 目标方法
     * @param targetClass  目标类
     */
    void logModule(LogRecord logRecord, Method targetMethod, Class<?> targetClass);

    /**
     * 获取日志包含信息
     *
     * @param includes     默认包含信息
     * @param targetMethod 目标方法
     * @param targetClass  目标类
     * @return 日志包含信息
     */
    Set<Include> getIncludes(Set<Include> includes, Method targetMethod, Class<?> targetClass);

    /**
     * 开始访问日志记录
     *
     * @param accessLogContext 访问日志上下文
     * @since 2.10.0
     */
    void accessLogStart(AccessLogContext accessLogContext);

    /**
     * 结束访问日志记录
     *
     * @param accessLogContext 访问日志上下文
     * @since 2.10.0
     */
    void accessLogFinish(AccessLogContext accessLogContext);
}
