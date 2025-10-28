package com.leepandar.starter.datapermission.model;

import com.leepandar.starter.datapermission.enums.DataScope;

/**
 * 角色数据
 */
public class RoleData {

    /**
     * 角色 ID
     */
    private Long roleId;

    /**
     * 数据权限
     */
    private DataScope dataScope;

    public RoleData() {
    }

    public RoleData(Long roleId, DataScope dataScope) {
        this.roleId = roleId;
        this.dataScope = dataScope;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public DataScope getDataScope() {
        return dataScope;
    }

    public void setDataScope(DataScope dataScope) {
        this.dataScope = dataScope;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RoleData roleData = (RoleData)o;
        return roleId.equals(roleData.roleId) && dataScope == roleData.dataScope;
    }

    @Override
    public int hashCode() {
        int result = roleId.hashCode();
        result = 31 * result + dataScope.hashCode();
        return result;
    }
}
