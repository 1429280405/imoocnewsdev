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
import java.util.Date;

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

    @ApiOperation(value = "查询文章列表", notes = "查询文章列表", httpMethod = "POST")
    @PostMapping("/queryAllList")
    public GraceJSONResult queryAllList(@RequestParam Integer status, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize);

    @PostMapping("queryMyList")
    @ApiOperation(value = "查询用户的所有文章列表", notes = "查询用户的所有文章列表", httpMethod = "POST")
    public GraceJSONResult queryMyList(@RequestParam String userId,
                                       @RequestParam String keyword,
                                       @RequestParam Integer status,
                                       @RequestParam Date startDate,
                                       @RequestParam Date endDate,
                                       @RequestParam(defaultValue = "1") Integer page,
                                       @RequestParam(defaultValue = "10") Integer pageSize);

    @ApiOperation(value = "修改审核状态", notes = "修改审核状态", httpMethod = "POST")
    @PostMapping("/doReview")
    public GraceJSONResult doReview(@RequestParam String articleId,
                                    @RequestParam String passOrNot);

    @ApiOperation(value = "删除文章", notes = "删除文章", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String articleId,
                                    @RequestParam String userId);

    @ApiOperation(value = "撤回文章", notes = "撤回文章", httpMethod = "POST")
    @PostMapping("/withdraw")
    public GraceJSONResult withdraw(@RequestParam String articleId,
                                  @RequestParam String userId);


}
