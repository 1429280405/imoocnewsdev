package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.SaveCategoryBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "文章分类维护", tags = {"文章分类维护"})
@RequestMapping("categoryMng")
public interface CategoryMngControllerApi {

    @ApiOperation(value = "新增或修改分类", notes = "新增或修改分类", httpMethod = "POST")
    @PostMapping("/saveOrUpdateCategory")
    public GraceJSONResult saveOrUpdateCategory(@RequestBody @Valid SaveCategoryBO newCategoryBO,
                                                BindingResult result);

    @ApiOperation(value = "查询分类列表", notes = "查询分类列表", httpMethod = "POST")
    @PostMapping("/getCatList")
    public GraceJSONResult getCatList();


    @ApiOperation(value = "用户端查询分类列表", notes = "用户端查询分类列表", httpMethod = "GET")
    @GetMapping("/getCats")
    public GraceJSONResult getCats();

}