package com.leepandar.starter.data.util;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;
import com.mybatisflex.core.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import com.leepandar.starter.core.exception.BadRequestException;
import com.leepandar.starter.core.util.ReflectUtils;
import com.leepandar.starter.core.util.validation.ValidationUtils;
import com.leepandar.starter.data.annotation.Query;
import com.leepandar.starter.data.enums.QueryType;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/**
 * QueryWrapper 助手
 */
public class QueryWrapperHelper {

    private static final Logger log = LoggerFactory.getLogger(QueryWrapperHelper.class);

    private QueryWrapperHelper() {
    }

    /**
     * 构建 QueryWrapper
     *
     * @param query 查询条件
     * @param <Q>   查询条件数据类型
     * @return QueryWrapper
     */
    public static <Q> QueryWrapper build(Q query) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        // 没有查询条件，直接返回
        if (query == null) {
            return queryWrapper;
        }
        // 获取查询条件中所有的字段
        List<Field> fieldList = ReflectUtils.getNonStaticFields(query.getClass());
        return build(query, fieldList, queryWrapper);
    }

    /**
     * 构建 QueryWrapper
     *
     * @param query 查询条件
     * @param sort  排序条件
     * @param <Q>   查询条件数据类型
     * @return QueryWrapper
     * @since 2.5.2
     */
    public static <Q> QueryWrapper build(Q query, Sort sort) {
        QueryWrapper queryWrapper = QueryWrapper.create();
        // 没有查询条件，直接返回
        if (query == null) {
            return queryWrapper;
        }
        // 设置排序条件
        if (sort != null && sort.isSorted()) {
            for (Sort.Order order : sort) {
                String field = CharSequenceUtil.toUnderlineCase(order.getProperty());
                ValidationUtils.throwIf(SqlInjectionUtils.check(field), "排序字段包含无效字符");
                queryWrapper.orderBy(field, order.isAscending());
            }
        }
        // 获取查询条件中所有的字段
        List<Field> fieldList = ReflectUtils.getNonStaticFields(query.getClass());
        return build(query, fieldList, queryWrapper);
    }

    /**
     * 构建 QueryWrapper
     *
     * @param query        查询条件
     * @param fields       查询条件字段列表
     * @param queryWrapper QueryWrapper
     * @param <Q>          查询条件数据类型
     * @return QueryWrapper
     */
    public static <Q> QueryWrapper build(Q query, List<Field> fields, QueryWrapper queryWrapper) {
        // 没有查询条件，直接返回
        if (query == null) {
            return queryWrapper;
        }
        // 解析并拼接查询条件
        for (Field field : fields) {
            List<Consumer<QueryWrapper>> consumers = buildWrapperConsumer(query, field);
            if (CollUtil.isNotEmpty(consumers)) {
                consumers.forEach(queryWrapper::and);
            }
        }
        return queryWrapper;
    }

    /**
     * 构建 QueryWrapper Consumer
     *
     * @param query 查询条件
     * @param field 查询条件字段
     * @param <Q>   查询条件数据类型
     * @param <R>   查询数据类型
     * @return QueryWrapper Consumer
     */
    private static <Q, R> List<Consumer<QueryWrapper>> buildWrapperConsumer(Q query, Field field) {
        try {
            // 如果字段值为空，直接返回
            Object fieldValue = ReflectUtil.getFieldValue(query, field);
            if (ObjectUtil.isEmpty(fieldValue)) {
                return Collections.emptyList();
            }
            // 没有 @Query 注解，直接返回
            Query queryAnnotation = AnnotationUtil.getAnnotation(field, Query.class);
            if (queryAnnotation == null) {
                return Collections.emptyList();
            }
            // 建议：数据库表列建议采用下划线连接法命名，程序变量建议采用驼峰法命名
            String fieldName = ReflectUtil.getFieldName(field);
            // 解析单列查询
            QueryType queryType = queryAnnotation.type();
            String[] columns = queryAnnotation.columns();
            final int columnLength = ArrayUtil.length(columns);
            List<Consumer<QueryWrapper>> consumers = new ArrayList<>(columnLength);
            if (columnLength <= 1) {
                String columnName = columnLength == 1 ? columns[0] : CharSequenceUtil.toUnderlineCase(fieldName);
                parse(queryType, columnName, fieldValue, consumers);
                return consumers;
            }
            // 解析多列查询
            for (String column : columns) {
                parse(queryType, column, fieldValue, consumers);
            }
            return consumers;
        } catch (BadRequestException e) {
            throw e;
        } catch (Exception e) {
            log.error("Build query wrapper occurred an error: {}. Query: {}, Field: {}.", e
                    .getMessage(), query, field, e);
        }
        return Collections.emptyList();
    }

    /**
     * 解析查询条件
     *
     * @param queryType  查询类型
     * @param columnName 列名
     * @param fieldValue 字段值
     */
    private static void parse(QueryType queryType,
                              String columnName,
                              Object fieldValue,
                              List<Consumer<QueryWrapper>> consumers) {
        switch (queryType) {
            case EQ -> consumers.add(q -> q.eq(columnName, fieldValue));
            case NE -> consumers.add(q -> q.ne(columnName, fieldValue));
            case GT -> consumers.add(q -> q.gt(columnName, fieldValue));
            case GE -> consumers.add(q -> q.ge(columnName, fieldValue));
            case LT -> consumers.add(q -> q.lt(columnName, fieldValue));
            case LE -> consumers.add(q -> q.le(columnName, fieldValue));
            case BETWEEN -> {
                List<Object> between = new ArrayList<>(ArrayUtil.isArray(fieldValue)
                        ? CollUtil.toList(fieldValue)
                        : (List<Object>) fieldValue);
                ValidationUtils.throwIf(between.size() != 2, "[{}] 必须是一个范围", columnName);
                consumers.add(q -> q.between(columnName, between.get(0), between.get(1)));
            }
            case LIKE -> consumers.add(q -> q.like(columnName, fieldValue));
            case LIKE_LEFT -> consumers.add(q -> q.likeLeft(columnName, fieldValue));
            case LIKE_RIGHT -> consumers.add(q -> q.likeRight(columnName, fieldValue));
            case IN -> {
                ValidationUtils.throwIfEmpty(fieldValue, "[{}] 不能为空", columnName);
                consumers.add(q -> q.in(columnName, ArrayUtil.isArray(fieldValue)
                        ? CollUtil.toList(fieldValue)
                        : (Collection<Object>) fieldValue));
            }
            case NOT_IN -> {
                ValidationUtils.throwIfEmpty(fieldValue, "[{}] 不能为空", columnName);
                consumers.add(q -> q.notIn(columnName, ArrayUtil.isArray(fieldValue)
                        ? CollUtil.toList(fieldValue)
                        : (Collection<Object>) fieldValue));
            }
            case IS_NULL -> consumers.add(q -> q.isNull(columnName));
            case IS_NOT_NULL -> consumers.add(q -> q.isNotNull(columnName));
            default -> throw new IllegalArgumentException("暂不支持 [%s] 查询类型".formatted(queryType));
        }
    }
}
