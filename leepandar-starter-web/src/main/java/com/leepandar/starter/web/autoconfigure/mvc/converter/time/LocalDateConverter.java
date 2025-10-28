package com.leepandar.starter.web.autoconfigure.mvc.converter.time;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;

/**
 * LocalDate 参数转换器
 */
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        return DateUtil.parse(source).toLocalDateTime().toLocalDate();
    }
}
