package com.leepandar.starter.crud.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.Range;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Sort;

import java.io.Serial;

/**
 * 分页查询条件
 */
@ParameterObject
@Schema(description = "分页查询条件")
public class PageQuery extends SortQuery {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 默认页码：1
     */
    private static final int DEFAULT_PAGE = 1;

    /**
     * 默认每页条数：10
     */
    private static final int DEFAULT_SIZE = 10;

    /**
     * 页码
     */
    @Schema(description = "页码", example = "1")
    @Min(value = 1, message = "页码最小值为 {value}")
    private Integer page = DEFAULT_PAGE;

    /**
     * 每页条数
     */
    @Schema(description = "每页条数", example = "10")
    @Range(min = 1, max = 1000, message = "每页条数（取值范围 {min}-{max}）")
    private Integer size = DEFAULT_SIZE;

    public PageQuery() {
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery(1, 10)}
     * </p>
     *
     * @param page 页码
     * @param size 每页条数
     * @since 2.12.0
     */
    public PageQuery(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery(1, 10, "createTime,desc", "name,asc")}
     * </p>
     *
     * @param page 页码
     * @param size 每页条数
     * @param sort 排序
     * @since 2.12.0
     */
    public PageQuery(Integer page, Integer size, String... sort) {
        super(sort);
        this.page = page;
        this.size = size;
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery("createTime,desc", "name,asc")}
     * </p>
     *
     * @param sort 排序
     * @since 2.12.0
     */
    public PageQuery(String... sort) {
        super(sort);
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery("createTime", Sort.Direction.DESC)}
     * </p>
     *
     * @param field     字段
     * @param direction 排序方向
     * @since 2.12.0
     */
    public PageQuery(String field, Sort.Direction direction) {
        super(field, direction);
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery(Sort.by(Sort.Direction.DESC, "createTime"))}
     * </p>
     *
     * @param sort 排序
     * @since 2.12.0
     */
    public PageQuery(Sort sort) {
        super(sort);
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery(1, 10, "createTime", Sort.Direction.DESC)}
     * </p>
     *
     * @param page      页码
     * @param size      每页条数
     * @param field     字段
     * @param direction 排序方向
     * @since 2.12.0
     */
    public PageQuery(Integer page, Integer size, String field, Sort.Direction direction) {
        super(field, direction);
        this.page = page;
        this.size = size;
    }

    /**
     * 构造方法
     *
     * <p>
     * 示例：{@code new PageQuery(1, 10, Sort.by(Sort.Direction.DESC, "createTime"))}
     * </p>
     *
     * @param page 页码
     * @param size 每页条数
     * @param sort 排序
     * @since 2.12.0
     */
    public PageQuery(Integer page, Integer size, Sort sort) {
        super(sort);
        this.page = page;
        this.size = size;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
