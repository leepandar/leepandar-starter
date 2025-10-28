package com.leepandar.starter.log.filter;

import cn.hutool.extra.spring.SpringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;
import com.leepandar.starter.core.wrapper.RepeatReadRequestWrapper;
import com.leepandar.starter.core.wrapper.RepeatReadResponseWrapper;
import com.leepandar.starter.log.model.LogProperties;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * 日志过滤器
 */
public class LogFilter extends OncePerRequestFilter {

    private final LogProperties logProperties;

    public LogFilter(LogProperties logProperties) {
        this.logProperties = logProperties;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (!this.isFilter(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        boolean isExcludeUri = logProperties.isMatch(request.getRequestURI());

        // 处理可重复读取的请求
        HttpServletRequest requestWrapper = (isExcludeUri || !this.isRequestWrapper(request))
            ? request
            : new RepeatReadRequestWrapper(request);

        // 处理可重复读取的响应
        HttpServletResponse responseWrapper = (isExcludeUri || !this.isResponseWrapper(response))
            ? response
            : new RepeatReadResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);

        // 如果响应被包装了，复制缓存数据到原始响应
        if (responseWrapper instanceof RepeatReadResponseWrapper wrappedResponse) {
            wrappedResponse.copyBodyToResponse();
        }
    }

    /**
     * 是否过滤请求
     *
     * @param request 请求对象
     * @return 是否过滤请求
     */
    private boolean isFilter(HttpServletRequest request) {
        if (!isRequestValid(request)) {
            return false;
        }
        // 不拦截 /error
        ServerProperties serverProperties = SpringUtil.getBean(ServerProperties.class);
        return !request.getRequestURI().equals(serverProperties.getError().getPath());
    }

    /**
     * 请求是否有效
     *
     * @param request 请求对象
     * @return true：是；false：否
     */
    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    /**
     * 是否需要包装输入流
     *
     * @param request 请求对象
     * @return true：是；false：否
     */
    private boolean isRequestWrapper(HttpServletRequest request) {
        return !(request instanceof RepeatReadRequestWrapper);
    }

    /**
     * 是否需要包装输出流
     *
     * @param response 响应对象
     * @return true：是；false：否
     */
    private boolean isResponseWrapper(HttpServletResponse response) {
        return !(response instanceof RepeatReadResponseWrapper);
    }
}
