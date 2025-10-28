package com.leepandar.starter.datapermission.exception;

import com.leepandar.starter.core.exception.BaseException;

/**
 * 数据权限异常
 */
public class DataPermissionException extends BaseException {

    public DataPermissionException(String message) {
        super(message);
    }

    public DataPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static DataPermissionException unsupportedDataScope(String dataScope) {
        return new DataPermissionException("Unsupported data scope: " + dataScope);
    }

    public static DataPermissionException unsupportedDatabase(String database) {
        return new DataPermissionException("Unsupported database for data permission: " + database);
    }

    public static DataPermissionException invalidUserData(String message) {
        return new DataPermissionException("Invalid user data: " + message);
    }

    public static DataPermissionException methodNotFound(String mappedStatementId) {
        return new DataPermissionException("Method not found for data permission: " + mappedStatementId);
    }
}