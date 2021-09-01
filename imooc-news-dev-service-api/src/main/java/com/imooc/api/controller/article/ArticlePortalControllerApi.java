package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "门户网文章controller", tags = {"门户网文章controller"})
@RequestMapping("portal/article")
public interface ArticlePortalControllerApi {

    @ApiOperation(value = "首页查询文章列表", notes = "首页查询文章列表", httpMethod = "GET")
    @GetMapping("/list")
    public GraceJSONResult createArticle(@RequestParam String keyword,
                                         @RequestParam Integer category,
                                         @ApiParam(name = "page", value = "查询下一页的第几页", required = false,defaultValue = "1")
                                             @RequestParam Integer page,
                                         @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false,defaultValue = "10")
                                             @RequestParam Integer pageSize);



    @ApiOperation(value = "查询最热新闻", notes = "查询最热新闻", httpMethod = "GET")
    @GetMapping("/hotList")
    public GraceJSONResult hotList();



}
