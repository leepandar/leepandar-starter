package com.leepandar.starter.crud.autoconfigure;

import cn.hutool.core.util.ArrayUtil;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPatternParser;
import com.leepandar.starter.core.util.ExceptionUtils;
import com.leepandar.starter.crud.annotation.CrudApi;
import com.leepandar.starter.crud.annotation.CrudRequestMapping;
import com.leepandar.starter.crud.enums.Api;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

/**
 * CRUD 请求映射器处理器映射器
 */
public class CrudRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    @Override
    protected RequestMappingInfo getMappingForMethod(@NonNull Method method, @NonNull Class<?> handlerType) {
        RequestMappingInfo requestMappingInfo = super.getMappingForMethod(method, handlerType);
        if (requestMappingInfo == null) {
            return null;
        }
        // 如果没有声明 @CrudRequestMapping 注解，直接返回
        if (!handlerType.isAnnotationPresent(CrudRequestMapping.class)) {
            return requestMappingInfo;
        }
        CrudRequestMapping crudRequestMapping = handlerType.getDeclaredAnnotation(CrudRequestMapping.class);
        CrudApi crudApi = AnnotatedElementUtils.findMergedAnnotation(method, CrudApi.class);
        // 过滤 API：如果非本类中定义，且 API 列表中不包含，则忽略
        Api[] apis = crudRequestMapping.api();
        if (method.getDeclaringClass() != handlerType && !ArrayUtil.contains(apis, ExceptionUtils
            .exToNull(crudApi::value))) {
            return null;
        }
        // 拼接路径（合并了 @RequestMapping 的部分能力）
        return this.getMappingForMethodWrapper(method, handlerType, crudRequestMapping);
    }

    /**
     * 获取请求映射信息
     *
     * @param method             方法
     * @param handlerType        处理程序类型
     * @param crudRequestMapping CRUD 请求映射
     * @return 请求映射信息
     */
    private RequestMappingInfo getMappingForMethodWrapper(@NonNull Method method,
                                                          @NonNull Class<?> handlerType,
                                                          CrudRequestMapping crudRequestMapping) {
        RequestMappingInfo info = this.buildRequestMappingInfo(method);
        if (info != null) {
            RequestMappingInfo typeInfo = this.buildRequestMappingInfo(handlerType);
            if (typeInfo != null) {
                info = typeInfo.combine(info);
            }
            String prefix = crudRequestMapping.value();
            if (prefix != null) {
                RequestMappingInfo.BuilderConfiguration options = new RequestMappingInfo.BuilderConfiguration();
                options.setPatternParser(PathPatternParser.defaultInstance);
                info = RequestMappingInfo.paths(prefix).options(options).build().combine(info);
            }
        }
        return info;
    }

    /**
     * 构建请求映射信息
     *
     * @param element 元素
     * @return 请求映射信息
     */
    private RequestMappingInfo buildRequestMappingInfo(AnnotatedElement element) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(element, RequestMapping.class);
        RequestCondition<?> condition = (element instanceof Class<?> clazz
            ? getCustomTypeCondition(clazz)
            : getCustomMethodCondition((Method)element));
        return (requestMapping != null ? super.createRequestMappingInfo(requestMapping, condition) : null);
    }
}
