package com.leepandar.starter.log.handler;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.ttl.TransmittableThreadLocal;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.leepandar.starter.log.annotation.Log;
import com.leepandar.starter.log.enums.Include;
import com.leepandar.starter.log.model.AccessLogContext;
import com.leepandar.starter.log.model.AccessLogProperties;
import com.leepandar.starter.log.model.LogRecord;
import com.leepandar.starter.log.util.AccessLogUtils;
import com.leepandar.starter.core.util.ServletUtils;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * 日志处理器基类
 */
public abstract class AbstractLogHandler implements LogHandler {

    private static final Logger log = LoggerFactory.getLogger(AbstractLogHandler.class);
    private final TransmittableThreadLocal<AccessLogContext> logContextThread = new TransmittableThreadLocal<>();

    @Override
    public boolean isRecord(Method targetMethod, Class<?> targetClass) {
        // 如果接口被隐藏，不记录日志
        Operation methodOperation = AnnotationUtil.getAnnotation(targetMethod, Operation.class);
        if (methodOperation != null && methodOperation.hidden()) {
            return false;
        }
        Hidden methodHidden = AnnotationUtil.getAnnotation(targetMethod, Hidden.class);
        if (methodHidden != null) {
            return false;
        }
        if (targetClass.getDeclaredAnnotation(Hidden.class) != null) {
            return false;
        }
        // 如果接口方法或类上有 @Log 注解，且要求忽略该接口，则不记录日志
        Log methodLog = AnnotationUtil.getAnnotation(targetMethod, Log.class);
        if (methodLog != null && methodLog.ignore()) {
            return false;
        }
        Log classLog = AnnotationUtil.getAnnotation(targetClass, Log.class);
        return classLog == null || !classLog.ignore();
    }

    @Override
    public LogRecord.Started start(Instant startTime) {
        return LogRecord.start(startTime);
    }

    @Override
    public LogRecord finish(LogRecord.Started started,
                            Instant endTime,
                            Set<Include> includes,
                            Method targetMethod,
                            Class<?> targetClass) {
        Set<Include> includeSet = this.getIncludes(includes, targetMethod, targetClass);
        LogRecord logRecord = this.finish(started, endTime, includeSet);
        // 记录日志描述
        if (includeSet.contains(Include.DESCRIPTION)) {
            this.logDescription(logRecord, targetMethod);
        }
        // 记录所属模块
        if (includeSet.contains(Include.MODULE)) {
            this.logModule(logRecord, targetMethod, targetClass);
        }
        return logRecord;
    }

    @Override
    public LogRecord finish(LogRecord.Started started, Instant endTime, Set<Include> includes) {
        return started.finish(endTime, includes);
    }

    /**
     * 记录日志描述
     *
     * @param logRecord    日志记录
     * @param targetMethod 目标方法
     */
    @Override
    public void logDescription(LogRecord logRecord, Method targetMethod) {
        logRecord.setDescription("请在该接口方法上添加 @top.continew.starter.log.annotation.Log(value) 来指定日志描述");
        Log methodLog = AnnotationUtil.getAnnotation(targetMethod, Log.class);
        // 例如：@Log("新增部门") -> 新增部门
        if (methodLog != null && CharSequenceUtil.isNotBlank(methodLog.value())) {
            logRecord.setDescription(methodLog.value());
            return;
        }
        // 例如：@Operation(summary="新增部门") -> 新增部门
        Operation methodOperation = AnnotationUtil.getAnnotation(targetMethod, Operation.class);
        if (methodOperation != null && CharSequenceUtil.isNotBlank(methodOperation.summary())) {
            logRecord.setDescription(methodOperation.summary());
        }
    }

    /**
     * 记录所属模块
     *
     * @param logRecord    日志记录
     * @param targetMethod 目标方法
     * @param targetClass  目标类
     */
    @Override
    public void logModule(LogRecord logRecord, Method targetMethod, Class<?> targetClass) {
        logRecord.setModule("请在该接口方法或类上添加 @top.continew.starter.log.annotation.Log(module) 来指定所属模块");
        Log methodLog = AnnotationUtil.getAnnotation(targetMethod, Log.class);
        // 例如：@Log(module = "部门管理") -> 部门管理
        // 方法级注解优先级高于类级注解
        if (methodLog != null && CharSequenceUtil.isNotBlank(methodLog.module())) {
            logRecord.setModule(methodLog.module());
            return;
        }
        Log classLog = AnnotationUtil.getAnnotation(targetClass, Log.class);
        if (classLog != null && CharSequenceUtil.isNotBlank(classLog.module())) {
            logRecord.setModule(classLog.module());
            return;
        }
        // 例如：@Tag(name = "部门管理") -> 部门管理
        Tag classTag = AnnotationUtil.getAnnotation(targetClass, Tag.class);
        if (classTag != null && CharSequenceUtil.isNotBlank(classTag.name())) {
            logRecord.setModule(classTag.name());
        }
    }

    @Override
    public Set<Include> getIncludes(Set<Include> includes, Method targetMethod, Class<?> targetClass) {
        Log classLog = AnnotationUtil.getAnnotation(targetClass, Log.class);
        Set<Include> includeSet = new HashSet<>(includes);
        if (classLog != null) {
            this.processInclude(includeSet, classLog);
        }
        // 方法级注解优先级高于类级注解
        Log methodLog = AnnotationUtil.getAnnotation(targetMethod, Log.class);
        if (methodLog != null) {
            this.processInclude(includeSet, methodLog);
        }
        return includeSet;
    }

    /**
     * 处理日志包含信息
     *
     * @param includes      日志包含信息
     * @param logAnnotation Log 注解
     */
    private void processInclude(Set<Include> includes, Log logAnnotation) {
        Include[] includeArr = logAnnotation.includes();
        if (includeArr.length > 0) {
            includes.addAll(Set.of(includeArr));
        }
        Include[] excludeArr = logAnnotation.excludes();
        if (excludeArr.length > 0) {
            includes.removeAll(Set.of(excludeArr));
        }
    }

    @Override
    public void accessLogStart(AccessLogContext accessLogContext) {
        AccessLogProperties properties = accessLogContext.getProperties().getAccessLog();
        // 是否需要打印 规则: 是否打印开关 或 放行路径
        if (!properties.isEnabled() || AccessLogUtils.exclusionPath(accessLogContext.getProperties(), ServletUtils
            .getRequestPath())) {
            return;
        }
        // 构建上下文
        logContextThread.set(accessLogContext);
        String param = AccessLogUtils.getParam(properties);
        log.info(param != null ? "[Start] [{}] {} param: {}" : "[Start] [{}] {}", ServletUtils
            .getRequestMethod(), ServletUtils.getRequestPath(), param);
    }

    @Override
    public void accessLogFinish(AccessLogContext accessLogContext) {
        AccessLogContext logContext = logContextThread.get();
        if (ObjectUtil.isEmpty(logContext)) {
            return;
        }
        try {
            Duration timeTaken = Duration.between(logContext.getStartTime(), accessLogContext.getEndTime());
            log.info("[End] [{}] {} {} {}ms", ServletUtils.getRequestMethod(), ServletUtils
                .getRequestPath(), ServletUtils.getResponseStatus(), timeTaken.toMillis());
        } finally {
            logContextThread.remove();
        }
    }
}
