package com.leepandar.starter.web.autoconfigure.response;

import com.feiniaojin.gracefulresponse.GracefulResponseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import com.leepandar.starter.core.constant.PropertiesConstants;

/**
 * 全局响应配置属性
 */
@ConfigurationProperties(PropertiesConstants.WEB_RESPONSE)
public class GlobalResponseProperties extends GracefulResponseProperties {}
