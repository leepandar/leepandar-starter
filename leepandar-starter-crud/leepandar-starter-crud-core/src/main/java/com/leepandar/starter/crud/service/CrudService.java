package com.leepandar.starter.crud.service;

import cn.hutool.core.lang.tree.Tree;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import com.leepandar.starter.crud.annotation.TreeField;
import com.leepandar.starter.crud.model.query.PageQuery;
import com.leepandar.starter.crud.model.query.SortQuery;
import com.leepandar.starter.crud.model.resp.BasePageResp;
import com.leepandar.starter.crud.model.resp.LabelValueResp;
import com.leepandar.starter.crud.validation.CrudValidationGroup;

import java.util.List;

/**
 * CRUD 业务接口
 *
 * @param <L> 列表类型
 * @param <D> 详情类型
 * @param <Q> 查询条件类型
 * @param <C> 创建或修改请求参数类型
 */
public interface CrudService<L, D, Q, C> {

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页列表信息
     */
    BasePageResp<L> page(@Valid Q query, @Valid PageQuery pageQuery);

    /**
     * 查询列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 列表信息
     */
    List<L> list(@Valid Q query, @Valid SortQuery sortQuery);

    /**
     * 查询树列表（多个根节点）
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param isSimple  是否为简单树结构（不包含基本树结构之外的扩展字段，简单树（例如：下拉列表）使用 CrudTreeDictModelProperties
     *                  全局树型字典映射配置，复杂树（例如：表格）使用 @TreeField 局部结构配置）
     * @return 树列表信息
     */
    List<Tree<Long>> tree(@Valid Q query, @Valid SortQuery sortQuery, boolean isSimple);

    /**
     * 查询树列表
     *
     * @param query        查询条件
     * @param sortQuery    排序查询条件
     * @param isSimple     是否为简单树结构（不包含基本树结构之外的扩展字段，简单树（下拉列表）使用全局配置结构，复杂树（表格）使用 @TreeField 局部配置）
     * @param isSingleRoot 是否为单个根节点
     * @return 树列表信息
     * @author lishuyan
     * @since 2.13.3
     * @see TreeField
     */
    List<Tree<Long>> tree(@Valid Q query, @Valid SortQuery sortQuery, boolean isSimple, boolean isSingleRoot);

    /**
     * 查询详情
     *
     * @param id ID
     * @return 详情信息
     */
    D get(Long id);

    /**
     * 创建
     *
     * @param req 创建请求参数
     * @return 自增 ID
     */
    @Validated(CrudValidationGroup.Create.class)
    Long create(@Valid C req);

    /**
     * 修改
     *
     * @param req 修改请求参数
     * @param id  ID
     */
    @Validated(CrudValidationGroup.Update.class)
    void update(@Valid C req, Long id);

    /**
     * 删除
     *
     * @param ids ID 列表
     */
    void delete(@NotEmpty(message = "ID 不能为空") List<Long> ids);

    /**
     * 导出
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param response  响应对象
     */
    void export(@Valid Q query, @Valid SortQuery sortQuery, HttpServletResponse response);

    /**
     * 查询字典列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 字典列表信息
     * @since 2.1.0
     */
    List<LabelValueResp> dict(@Valid Q query, @Valid SortQuery sortQuery);
}
