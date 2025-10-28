package com.leepandar.starter.web.autoconfigure.response;

import com.feiniaojin.gracefulresponse.advice.lifecycle.exception.BeforeControllerAdviceProcess;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;

/**
 * 默认回调处理器实现
 *
 * @author Charles7c
 * @since 2.6.0
 */
public class DefaultBeforeControllerAdviceProcessImpl implements BeforeControllerAdviceProcess {

    private final Logger log = LoggerFactory.getLogger(DefaultBeforeControllerAdviceProcessImpl.class);
    private final GlobalResponseProperties globalResponseProperties;

    public DefaultBeforeControllerAdviceProcessImpl(GlobalResponseProperties globalResponseProperties) {
        this.globalResponseProperties = globalResponseProperties;
    }

    @Override
    public void call(HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception e) {
        if (globalResponseProperties.isPrintExceptionInGlobalAdvice()) {
            log.error("[{}] {}", request.getMethod(), request.getRequestURI(), e);
        }
    }
}
