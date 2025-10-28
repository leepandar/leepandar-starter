package com.leepandar.starter.crud.controller;

import cn.hutool.core.lang.tree.Tree;
import com.feiniaojin.gracefulresponse.api.ExcludeFromGracefulResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.leepandar.starter.crud.annotation.CrudApi;
import com.leepandar.starter.crud.enums.Api;
import com.leepandar.starter.crud.handler.CrudApiHandler;
import com.leepandar.starter.crud.model.query.PageQuery;
import com.leepandar.starter.crud.model.query.SortQuery;
import com.leepandar.starter.crud.model.req.IdsReq;
import com.leepandar.starter.crud.model.resp.IdResp;
import com.leepandar.starter.crud.model.resp.BasePageResp;
import com.leepandar.starter.crud.model.resp.LabelValueResp;
import com.leepandar.starter.crud.service.CrudService;
import com.leepandar.starter.crud.validation.CrudValidationGroup;

import java.util.List;

/**
 * CRUD 控制器抽象基类
 *
 * @param <S> 业务接口
 * @param <L> 列表类型
 * @param <D> 详情类型
 * @param <Q> 查询条件类型
 * @param <C> 创建或修改请求参数类型
 */
public abstract class AbstractCrudController<S extends CrudService<L, D, Q, C>, L, D, Q, C> implements CrudApiHandler {

    @Autowired
    protected S baseService;

    /**
     * 分页查询列表
     *
     * @param query     查询条件
     * @param pageQuery 分页查询条件
     * @return 分页信息
     */
    @CrudApi(Api.PAGE)
    @Operation(summary = "分页查询列表", description = "分页查询列表")
    @ResponseBody
    @GetMapping
    public BasePageResp<L> page(@Valid Q query, @Valid PageQuery pageQuery) {
        return baseService.page(query, pageQuery);
    }

    /**
     * 查询列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 列表信息
     */
    @CrudApi(Api.LIST)
    @Operation(summary = "查询列表", description = "查询列表")
    @ResponseBody
    @GetMapping("/list")
    public List<L> list(@Valid Q query, @Valid SortQuery sortQuery) {
        return baseService.list(query, sortQuery);
    }

    /**
     * 查询树列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 树列表信息
     */
    @CrudApi(Api.TREE)
    @Operation(summary = "查询树列表", description = "查询树列表")
    @ResponseBody
    @GetMapping("/tree")
    public List<Tree<Long>> tree(@Valid Q query, @Valid SortQuery sortQuery) {
        return baseService.tree(query, sortQuery, false);
    }

    /**
     * 查询详情
     *
     * @param id ID
     * @return 详情信息
     */
    @CrudApi(Api.GET)
    @Operation(summary = "查询详情", description = "查询详情")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @ResponseBody
    @GetMapping("/{id}")
    public D get(@PathVariable("id") Long id) {
        return baseService.get(id);
    }

    /**
     * 创建
     *
     * @param req 创建请求参数
     * @return ID
     */
    @CrudApi(Api.CREATE)
    @Operation(summary = "创建数据", description = "创建数据")
    @ResponseBody
    @PostMapping
    @Validated(CrudValidationGroup.Create.class)
    public IdResp<Long> create(@RequestBody @Valid C req) {
        return new IdResp<>(baseService.create(req));
    }

    /**
     * 修改
     *
     * @param req 修改请求参数
     * @param id  ID
     */
    @CrudApi(Api.UPDATE)
    @Operation(summary = "修改数据", description = "修改数据")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @ResponseBody
    @PutMapping("/{id}")
    @Validated(CrudValidationGroup.Update.class)
    public void update(@RequestBody @Valid C req, @PathVariable("id") Long id) {
        baseService.update(req, id);
    }

    /**
     * 删除
     *
     * @param id ID
     */
    @CrudApi(Api.DELETE)
    @Operation(summary = "删除数据", description = "删除数据")
    @Parameter(name = "id", description = "ID", example = "1", in = ParameterIn.PATH)
    @ResponseBody
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        baseService.delete(List.of(id));
    }

    /**
     * 批量删除
     *
     * @param req 删除请求参数
     */
    @CrudApi(Api.BATCH_DELETE)
    @Operation(summary = "批量删除数据", description = "批量删除数据")
    @ResponseBody
    @DeleteMapping
    public void batchDelete(@RequestBody @Valid IdsReq req) {
        baseService.delete(req.getIds());
    }

    /**
     * 导出
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @param response  响应对象
     */
    @CrudApi(Api.EXPORT)
    @ExcludeFromGracefulResponse
    @Operation(summary = "导出数据", description = "导出数据")
    @GetMapping("/export")
    public void export(@Valid Q query, @Valid SortQuery sortQuery, HttpServletResponse response) {
        baseService.export(query, sortQuery, response);
    }

    /**
     * 查询字典列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 字典列表信息
     */
    @CrudApi(Api.DICT)
    @Operation(summary = "查询字典列表", description = "查询字典列表（下拉选项等场景）")
    @GetMapping("/dict")
    public List<LabelValueResp> dict(@Valid Q query, @Valid SortQuery sortQuery) {
        return baseService.dict(query, sortQuery);
    }

    /**
     * 查询树型字典列表
     *
     * @param query     查询条件
     * @param sortQuery 排序查询条件
     * @return 树型字典列表信息
     */
    @CrudApi(Api.TREE_DICT)
    @Operation(summary = "查询树型字典列表", description = "查询树型结构字典列表（树型结构下拉选项等场景）")
    @GetMapping("/dict/tree")
    public List<Tree<Long>> treeDict(@Valid Q query, @Valid SortQuery sortQuery) {
        return baseService.tree(query, sortQuery, true);
    }
}
