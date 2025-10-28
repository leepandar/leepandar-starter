package com.leepandar.starter.crud.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import com.leepandar.starter.core.constant.PropertiesConstants;
import com.leepandar.starter.crud.model.query.SortQuery;

/**
 * CRUD 配置属性
 */
@ConfigurationProperties(PropertiesConstants.CRUD)
public class CrudProperties {

    /**
     * 树型字典结构映射配置
     */
    @NestedConfigurationProperty
    private CrudTreeDictModelProperties treeDictModel = new CrudTreeDictModelProperties();

    public CrudTreeDictModelProperties getTreeDictModel() {
        return treeDictModel;
    }

    public void setTreeDictModel(CrudTreeDictModelProperties treeDictModel) {
        this.treeDictModel = treeDictModel;
    }
}
