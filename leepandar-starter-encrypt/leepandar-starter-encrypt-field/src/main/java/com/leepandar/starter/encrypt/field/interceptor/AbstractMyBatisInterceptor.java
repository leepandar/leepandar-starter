package com.leepandar.starter.encrypt.field.interceptor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.mapping.MappedStatement;
import com.leepandar.starter.core.constant.StringConstants;
import com.leepandar.starter.core.exception.BaseException;
import com.leepandar.starter.encrypt.field.annotation.FieldEncrypt;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

/**
 * 字段解密拦截器
 */
public abstract class AbstractMyBatisInterceptor {

    private static final Map<Class<?>, List<Field>> CLASS_FIELD_CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Map<String, FieldEncrypt>> ENCRYPT_PARAM_CACHE = new ConcurrentHashMap<>();

    /**
     * 获取所有字符串类型、需要加/解密的、有值字段
     *
     * @param obj 对象
     * @return 字段列表
     */
    protected List<Field> getEncryptFields(Object obj) {
        if (obj == null) {
            return Collections.emptyList();
        }
        return this.getEncryptFields(obj.getClass());
    }

    /**
     * 获取所有字符串类型、需要加/解密的、有值字段
     *
     * @param clazz 类型对象
     * @return 字段列表
     */
    protected List<Field> getEncryptFields(Class<?> clazz) {
        return CLASS_FIELD_CACHE.computeIfAbsent(clazz, key -> Arrays.stream(ReflectUtil.getFields(clazz))
            .filter(field -> String.class.equals(field.getType()))
            .filter(field -> field.getAnnotation(FieldEncrypt.class) != null)
            .toList());
    }

    /**
     * 获取加密参数
     *
     * @param mappedStatement 映射语句
     * @return 获取加密参数
     */
    protected Map<String, FieldEncrypt> getEncryptParameters(MappedStatement mappedStatement) {
        String mappedStatementId = mappedStatement.getId();
        return ENCRYPT_PARAM_CACHE.computeIfAbsent(mappedStatementId, key -> {
            Method method = this.getMethod(mappedStatementId);
            if (method == null) {
                return Collections.emptyMap();
            }
            Map<String, FieldEncrypt> encryptMap = new HashMap<>();
            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameter = parameters[i];
                FieldEncrypt fieldEncrypt = parameter.getAnnotation(FieldEncrypt.class);
                if (fieldEncrypt == null) {
                    continue;
                }
                String parameterName = this.getParameterName(parameter);
                encryptMap.put(parameterName, fieldEncrypt);
                if (String.class.equals(parameter.getType())) {
                    encryptMap.put("param" + (i + 1), fieldEncrypt);
                }
            }
            return encryptMap;
        });
    }

    /**
     * 获取映射方法
     *
     * @param mappedStatementId 映射语句 ID
     * @return 映射方法
     */
    private Method getMethod(String mappedStatementId) {
        String className = CharSequenceUtil.subBefore(mappedStatementId, StringConstants.DOT, true);
        String methodName = CharSequenceUtil.subAfter(mappedStatementId, StringConstants.DOT, true);
        try {
            Method[] methods = ReflectUtil.getMethods(Class.forName(className));
            return Stream.of(methods).filter(method -> method.getName().equals(methodName)).findFirst().orElse(null);
        } catch (ClassNotFoundException e) {
            throw new BaseException(e);
        }
    }

    /**
     * 获取参数名称
     *
     * @param parameter 参数
     * @return 参数名称
     */
    public String getParameterName(Parameter parameter) {
        Param param = parameter.getAnnotation(Param.class);
        return param != null ? param.value() : parameter.getName();
    }
}