package com.leepandar.starter.web.autoconfigure.mvc.converter;

import org.springframework.core.convert.converter.Converter;
import com.leepandar.starter.core.enums.BaseEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * BaseEnum 参数转换器
 */
public class BaseEnumConverter<T extends BaseEnum> implements Converter<String, T> {

    private final Map<String, T> enumMap = new HashMap<>();

    public BaseEnumConverter(Class<T> enumType) {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums) {
            enumMap.put(String.valueOf(e.getValue()), e);
        }
    }

    @Override
    public T convert(String source) {
        return enumMap.get(source);
    }
}
