package com.imooc.api.controller.article;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "评论相关业务的controller", tags = {"评论相关业务的controller"})
@RequestMapping("comment")
public interface CommentControllerApi {

    @ApiOperation(value = "用户评论", notes = "用户评论", httpMethod = "POST")
    @PostMapping("/createComment")
    public GraceJSONResult createComment(@RequestBody @Valid CommentReplyBO commentReplyBO,
                                         BindingResult result);

    @ApiOperation(value = "用户评论数查询", notes = "用户评论数查询", httpMethod = "GET")
    @GetMapping("/counts")
    public GraceJSONResult commentCounts(@RequestParam String articleId);


    @ApiOperation(value = "查询文章的所有评论列表", notes = "查询文章的所有评论列表", httpMethod = "GET")
    @GetMapping("/list")
    public GraceJSONResult list(@RequestParam String articleId,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer pageSize);


    @ApiOperation(value = "查询我的评论管理列表", notes = "查询我的评论管理列表", httpMethod = "POST")
    @PostMapping("/mng")
    public GraceJSONResult mng(@RequestParam String writerId,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer pageSize);


    @ApiOperation(value = "作者删除评论", notes = "作者删除评论", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String writerId,
                                  @RequestParam String commentId);


}
