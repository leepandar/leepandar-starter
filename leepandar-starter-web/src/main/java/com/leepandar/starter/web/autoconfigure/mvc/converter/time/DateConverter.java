package com.leepandar.starter.web.autoconfigure.mvc.converter.time;

import cn.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * Date 参数转换器
 */
public class DateConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        return DateUtil.parse(source);
    }
}
