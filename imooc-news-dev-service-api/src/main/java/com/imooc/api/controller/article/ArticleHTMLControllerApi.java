package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.NewArticleBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "文章静态化controller", tags = {"文章静态化controller"})
@RequestMapping("article/html")
public interface ArticleHTMLControllerApi {

    @ApiOperation(value = "下载html", notes = "下载html", httpMethod = "GET")
    @GetMapping("/download")
    public Integer download(@RequestParam String articleId,
                            @RequestParam String articleMongoId) throws Exception;


}
