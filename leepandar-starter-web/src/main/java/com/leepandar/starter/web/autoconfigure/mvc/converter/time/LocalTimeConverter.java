package com.leepandar.starter.web.autoconfigure.mvc.converter.time;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalTime;

/**
 * LocalTime 参数转换器
 */
public class LocalTimeConverter implements Converter<String, LocalTime> {

    @Override
    public LocalTime convert(String source) {
        return DateUtil.parse(source).toLocalDateTime().toLocalTime();
    }
}
