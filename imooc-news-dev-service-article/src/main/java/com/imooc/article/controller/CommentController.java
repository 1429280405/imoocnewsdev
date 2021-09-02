package com.imooc.article.controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.article.CommentControllerApi;
import com.imooc.article.service.CommentService;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.CommentReplyBO;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-09-02 20:36
 */
@RestController
public class CommentController extends BaseController implements CommentControllerApi {


    @Autowired
    private CommentService commentService;


    @Override
    public GraceJSONResult createComment(@Valid CommentReplyBO commentReplyBO, BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = getErrors(result);
            return GraceJSONResult.errorMap(errors);
        }
        String userId = commentReplyBO.getCommentUserId();
        //发起rest请求，获得用户
        HashSet<String> idSet = new HashSet<>();
        idSet.add(userId);
        String nickname = getBasicUserList(idSet).get(0).getNickname();
        //保存用户评论至数据库
        commentService.createComment(commentReplyBO, nickname);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult commentCounts(String articleId) {
        Integer counts = getCountsFromRedis(REDIS_ARTICLE_COMMENT_COUNTS + ":" + articleId);
        return GraceJSONResult.ok(counts);
    }
}
