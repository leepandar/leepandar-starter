package com.leepandar.starter.log.interceptor;

import com.alibaba.ttl.TransmittableThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.leepandar.starter.log.dao.LogDao;
import com.leepandar.starter.log.handler.LogHandler;
import com.leepandar.starter.log.model.AccessLogContext;
import com.leepandar.starter.log.model.LogProperties;
import com.leepandar.starter.log.model.LogRecord;

import java.lang.reflect.Method;
import java.time.Instant;

/**
 * 日志拦截器
 */
public class LogInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(LogInterceptor.class);
    private final LogProperties logProperties;
    private final LogHandler logHandler;
    private final LogDao logDao;
    private final TransmittableThreadLocal<LogRecord.Started> logTtl = new TransmittableThreadLocal<>();

    public LogInterceptor(LogProperties logProperties, LogHandler logHandler, LogDao logDao) {
        this.logProperties = logProperties;
        this.logHandler = logHandler;
        this.logDao = logDao;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {
        Instant startTime = Instant.now();
        logHandler.accessLogStart(AccessLogContext.builder().startTime(startTime).properties(logProperties).build());
        // 开始日志记录
        if (this.isRecord(handler)) {
            LogRecord.Started startedLogRecord = logHandler.start(startTime);
            logTtl.set(startedLogRecord);
        }
        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request,
                                @NonNull HttpServletResponse response,
                                @NonNull Object handler,
                                Exception e) {
        try {
            Instant endTime = Instant.now();
            logHandler.accessLogFinish(AccessLogContext.builder().endTime(endTime).build());
            LogRecord.Started startedLogRecord = logTtl.get();
            if (startedLogRecord == null) {
                return;
            }
            // 结束日志记录
            HandlerMethod handlerMethod = (HandlerMethod)handler;
            Method targetMethod = handlerMethod.getMethod();
            Class<?> targetClass = handlerMethod.getBeanType();
            LogRecord logRecord = logHandler.finish(startedLogRecord, endTime, logProperties
                .getIncludes(), targetMethod, targetClass);
            logDao.add(logRecord);
        } catch (Exception ex) {
            log.error("Logging http log occurred an error: {}.", ex.getMessage(), ex);
            throw ex;
        } finally {
            logTtl.remove();
        }
    }

    /**
     * 是否记录日志
     *
     * @param handler 处理器
     * @return true：需要记录；false：不需要记录
     */
    private boolean isRecord(Object handler) {
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return false;
        }
        return logHandler.isRecord(handlerMethod.getMethod(), handlerMethod.getBeanType());
    }
}
