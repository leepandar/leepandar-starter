package com.leepandar.starter.datapermission.model;

import java.util.Collections;
import java.util.Set;

/**
 * 用户数据
 */
public class UserData {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 角色列表
     */
    private Set<RoleData> roles = Collections.emptySet();

    /**
     * 部门 ID
     */
    private Long deptId;

    public UserData() {
    }

    public UserData(Long userId, Long deptId, Set<RoleData> roles) {
        this.userId = userId;
        this.deptId = deptId;
        this.roles = roles != null ? roles : Collections.emptySet();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Set<RoleData> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleData> roles) {
        this.roles = roles != null ? roles : Collections.emptySet();
    }

    public Long getDeptId() {
        return deptId;
    }

    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }

    /**
     * 检查用户数据是否有效
     *
     * @return 是否有效
     */
    public boolean isValid() {
        return userId != null && deptId != null && !roles.isEmpty();
    }
}
