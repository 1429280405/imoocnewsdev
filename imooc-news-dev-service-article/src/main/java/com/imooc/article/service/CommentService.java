package com.imooc.article.service;

import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.utils.PagedGridResult;

/**
 * @author liujinqiang
 * @create 2021-09-02 21:10
 */
public interface CommentService {
    void createComment(CommentReplyBO commentReplyBO, String nickname);

    PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize);

    PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize);

    void deleteComment(String writerId, String commentId);
}
