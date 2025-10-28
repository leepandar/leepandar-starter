package com.leepandar.starter.data.autoconfigure.idgenerator;

import com.leepandar.starter.data.enums.MyBatisPlusIdGeneratorType;

/**
 * MyBatis ID 生成器配置属性
 */
public class MyBatisPlusIdGeneratorProperties {

    /**
     * ID 生成器类型
     */
    private MyBatisPlusIdGeneratorType type = MyBatisPlusIdGeneratorType.DEFAULT;

    public MyBatisPlusIdGeneratorType getType() {
        return type;
    }

    public void setType(MyBatisPlusIdGeneratorType type) {
        this.type = type;
    }
}
