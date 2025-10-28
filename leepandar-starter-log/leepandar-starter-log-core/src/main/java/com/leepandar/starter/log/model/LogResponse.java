package com.leepandar.starter.log.model;

import com.leepandar.starter.log.enums.Include;
import com.leepandar.starter.core.util.ServletUtils;

import java.util.Map;
import java.util.Set;

/**
 * 响应信息
 */
public class LogResponse {

    /**
     * 状态码
     */
    private Integer status;

    /**
     * 响应头
     */
    private Map<String, String> headers;

    /**
     * 响应体（JSON 字符串）
     */
    private String body;

    /**
     * 响应参数
     */
    private Map<String, Object> param;

    public LogResponse(Set<Include> includes) {
        this.status = ServletUtils.getResponseStatus();
        this.headers = (includes.contains(Include.RESPONSE_HEADERS)) ? ServletUtils.getResponseHeaders() : null;
        if (includes.contains(Include.RESPONSE_BODY)) {
            this.body = ServletUtils.getResponseBody();
        } else if (includes.contains(Include.RESPONSE_PARAM)) {
            this.param = ServletUtils.getResponseParams();
        }
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
}