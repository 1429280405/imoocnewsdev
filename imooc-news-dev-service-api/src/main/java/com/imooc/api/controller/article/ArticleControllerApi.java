package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "文章业务controller", tags = {"文章业务controller"})
@RequestMapping("article")
public interface ArticleControllerApi {

    @ApiOperation(value = "新建文章", notes = "新建文章", httpMethod = "POST")
    @PostMapping("/createArticle")
    public GraceJSONResult createArticle(@RequestBody @Valid NewArticleBO newArticleBO,
                                         BindingResult result);



}
