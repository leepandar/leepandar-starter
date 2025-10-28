package com.leepandar.starter.crud.autoconfigure;

import cn.hutool.core.lang.tree.TreeNodeConfig;
import com.leepandar.starter.core.util.validation.CheckUtils;
import com.leepandar.starter.crud.annotation.TreeField;

/**
 * 树型字典结构映射配置属性
 * 用于查询树型结构字典列表 API（树型结构下拉选项等场景）
 */
public class CrudTreeDictModelProperties {

    /**
     * ID 字段名
     */
    private String idKey = "id";

    /**
     * 父 ID 字段名
     */
    private String parentIdKey = "parentId";

    /**
     * 名称字段名
     */
    private String nameKey = "name";

    /**
     * 排序字段名
     */
    private String weightKey = "weight";

    /**
     * 子列表字段名
     */
    private String childrenKey = "children";

    /**
     * 递归深度（< 0 不限制）
     */
    private Integer deep = -1;

    /**
     * 根节点 ID
     */
    private Long rootId = 0L;

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getParentIdKey() {
        return parentIdKey;
    }

    public void setParentIdKey(String parentIdKey) {
        this.parentIdKey = parentIdKey;
    }

    public String getNameKey() {
        return nameKey;
    }

    public void setNameKey(String nameKey) {
        this.nameKey = nameKey;
    }

    public String getWeightKey() {
        return weightKey;
    }

    public void setWeightKey(String weightKey) {
        this.weightKey = weightKey;
    }

    public String getChildrenKey() {
        return childrenKey;
    }

    public void setChildrenKey(String childrenKey) {
        this.childrenKey = childrenKey;
    }

    public Integer getDeep() {
        return deep;
    }

    public void setDeep(Integer deep) {
        this.deep = deep;
    }

    public Long getRootId() {
        return rootId;
    }

    public void setRootId(Long rootId) {
        this.rootId = rootId;
    }

    /**
     * 生成 {@link TreeNodeConfig} 对象
     *
     * @return {@link TreeNodeConfig} 对象
     */
    public TreeNodeConfig genTreeNodeConfig() {
        return TreeNodeConfig.DEFAULT_CONFIG.setIdKey(idKey)
            .setParentIdKey(parentIdKey)
            .setNameKey(nameKey)
            .setWeightKey(weightKey)
            .setChildrenKey(childrenKey)
            .setDeep(deep < 0 ? null : deep);
    }

    /**
     * 根据 @TreeField 配置生成树结构配置
     *
     * @param treeField 树结构字段注解
     * @return 树结构配置
     */
    public TreeNodeConfig genTreeNodeConfig(TreeField treeField) {
        CheckUtils.throwIfNull(treeField, "请添加并配置 @TreeField 树结构信息");
        return new TreeNodeConfig().setIdKey(treeField.value())
            .setParentIdKey(treeField.parentIdKey())
            .setNameKey(treeField.nameKey())
            .setWeightKey(treeField.weightKey())
            .setChildrenKey(treeField.childrenKey())
            .setDeep(treeField.deep() < 0 ? null : treeField.deep());
    }
}
