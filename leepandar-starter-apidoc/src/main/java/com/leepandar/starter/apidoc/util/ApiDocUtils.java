package com.leepandar.starter.apidoc.util;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.web.bind.annotation.RestController;
import com.leepandar.starter.core.enums.BaseEnum;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 接口文档工具类
 */
public class ApiDocUtils {

    private ApiDocUtils() {
    }

    /**
     * 构建分组接口文档
     *
     * @param group        分组名称
     * @param pathsToMatch 路径匹配
     * @return GroupedOpenApi
     * @since 2.13.0
     */
    public static GroupedOpenApi buildGroupedOpenApi(String group, String... pathsToMatch) {
        return buildGroupedOpenApi(group, group, pathsToMatch);
    }

    /**
     * 构建分组接口文档
     *
     * @param group        分组名称
     * @param displayName  分组显示名称
     * @param pathsToMatch 路径匹配
     * @return GroupedOpenApi
     * @since 2.13.0
     */
    public static GroupedOpenApi buildGroupedOpenApi(String group, String displayName, String... pathsToMatch) {
        return GroupedOpenApi.builder().group(group).displayName(displayName).pathsToMatch(pathsToMatch).build();
    }

    /**
     * 获取枚举值类型
     *
     * @param enumClass 枚举类型
     * @return 枚举值类型
     */
    public static String getEnumValueTypeAsString(Class<?> enumClass) {
        // 获取枚举类实现的所有接口
        Type[] interfaces = enumClass.getGenericInterfaces();
        // 定义枚举值类型的映射
        Map<Class<?>, String> typeMap = Map
            .of(Integer.class, "integer", Long.class, "long", Double.class, "number", String.class, "string");
        // 遍历所有接口
        for (Type type : interfaces) {
            // 检查接口是否为参数化类型并且原始类型为 BaseEnum
            if (type instanceof ParameterizedType parameterizedType && parameterizedType
                .getRawType() == BaseEnum.class) {
                Type actualType = parameterizedType.getActualTypeArguments()[0];
                // 检查实际类型参数是否为类类型，并返回对应的字符串类型
                if (actualType instanceof Class<?> actualClass) {
                    return typeMap.getOrDefault(actualClass, "string");
                }
            }
        }
        // 默认返回 "string" 类型
        return "string";
    }

    /**
     * 解析枚举值的格式
     *
     * @param enumValueType 枚举值类型
     * @return String 格式化类型
     */
    public static String resolveFormat(String enumValueType) {
        return switch (enumValueType) {
            case "integer" -> "int32";
            case "long" -> "int64";
            case "number" -> "double";
            default -> enumValueType;
        };
    }

    /**
     * 具有 RestController 注释，既检查是否继承了BaseController
     *
     * @param clazz clazz
     * @return boolean
     */
    public static boolean hasRestControllerAnnotation(Class<?> clazz) {
        // 如果注释包含 RestController 注解，则返回 true
        if (clazz.isAnnotationPresent(RestController.class)) {
            return true;
        }
        // 递归检查父类
        Class<?> superClass = clazz.getSuperclass();
        // 循环检查父类
        while (superClass != null && !superClass.equals(Object.class)) {
            // 如果父类包含 RestController 注解，则返回 true
            if (hasRestControllerAnnotation(superClass)) {
                return true;
            }
            // 递归检查接口
            superClass = superClass.getSuperclass();
        }
        return false;
    }

    /**
     * 获取枚举描述 Map
     *
     * @param enumClass 枚举类型
     * @return 枚举描述 Map
     */
    public static Map<Object, String> getDescMap(Class<?> enumClass) {
        BaseEnum[] enums = (BaseEnum[])enumClass.getEnumConstants();
        return Arrays.stream(enums)
            .collect(Collectors.toMap(BaseEnum::getValue, BaseEnum::getDescription, (a, b) -> a, LinkedHashMap::new));
    }
}