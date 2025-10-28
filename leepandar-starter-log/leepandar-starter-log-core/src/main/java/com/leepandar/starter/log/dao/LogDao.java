package com.leepandar.starter.log.dao;

import com.leepandar.starter.log.model.LogRecord;

import java.util.Collections;
import java.util.List;

/**
 * 日志持久层接口
 */
public interface LogDao {

    /**
     * 查询日志列表
     *
     * @return 日志列表
     */
    default List<LogRecord> list() {
        return Collections.emptyList();
    }

    /**
     * 记录日志
     *
     * @param logRecord 日志信息
     */
    void add(LogRecord logRecord);
}
