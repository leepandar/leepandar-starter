package com.leepandar.starter.crud.model.req;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * ID 请求参数
 */
public class IdReq implements Serializable {

    /**
     * ID
     */
    @Schema(description = "ID", example = "1")
    @NotNull(message = "ID 不能为空")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
