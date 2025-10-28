package com.leepandar.starter.encrypt.api.filter;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ObjectUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import com.leepandar.starter.core.util.SpringWebUtils;
import com.leepandar.starter.encrypt.api.annotation.ApiEncrypt;
import com.leepandar.starter.encrypt.api.autoconfigure.ApiEncryptProperties;

import java.io.IOException;
import java.util.Optional;

/**
 * API 加密过滤器
 */
public class ApiEncryptFilter implements Filter {

    private final ApiEncryptProperties properties;

    public ApiEncryptFilter(ApiEncryptProperties properties) {
        this.properties = properties;
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        // 是否加密响应
        boolean isResponseEncrypt = this.isResponseEncrypt(request);
        // 密钥标头
        String secretKeyHeader = properties.getSecretKeyHeader();
        ServletRequest requestWrapper = null;
        ServletResponse responseWrapper = null;
        ResponseBodyEncryptWrapper responseBodyEncryptWrapper = null;
        // 是否为 PUT 或者 POST 请求
        if (HttpMethod.PUT.matches(request.getMethod()) || HttpMethod.POST.matches(request.getMethod())) {
            // 获取密钥值
            String secretKeyValue = request.getHeader(secretKeyHeader);
            if (CharSequenceUtil.isNotBlank(secretKeyValue)) {
                // 请求解密
                requestWrapper = new RequestBodyDecryptWrapper(request, properties.getPrivateKey(), secretKeyHeader);
            }
        }
        // 响应加密，响应包装器替换响应体加密包装器
        if (isResponseEncrypt) {
            responseBodyEncryptWrapper = new ResponseBodyEncryptWrapper(response);
            responseWrapper = responseBodyEncryptWrapper;
        }
        // 继续执行
        chain.doFilter(ObjectUtil.defaultIfNull(requestWrapper, request), ObjectUtil
            .defaultIfNull(responseWrapper, response));
        // 响应加密，执行完成后，响应密文
        if (isResponseEncrypt) {
            servletResponse.reset();
            // 获取密文
            String encryptContent = responseBodyEncryptWrapper.getEncryptContent(response, properties
                .getPublicKey(), secretKeyHeader);
            // 写出密文
            servletResponse.getWriter().write(encryptContent);
        }
    }

    /**
     * 是否加密响应
     *
     * @param request 请求对象
     * @return 是否加密响应
     */
    private boolean isResponseEncrypt(HttpServletRequest request) {
        // 获取 API 加密注解
        ApiEncrypt apiEncrypt = Optional.ofNullable(SpringWebUtils.getHandlerMethod(request))
            .map(h -> h.getMethodAnnotation(ApiEncrypt.class))
            .orElse(null);
        return apiEncrypt != null && apiEncrypt.response();
    }
}