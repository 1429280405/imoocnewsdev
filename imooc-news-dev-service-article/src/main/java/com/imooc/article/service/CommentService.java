package com.imooc.article.service;

import com.imooc.pojo.bo.CommentReplyBO;

/**
 * @author liujinqiang
 * @create 2021-09-02 21:10
 */
public interface CommentService {
    void createComment(CommentReplyBO commentReplyBO, String nickname);
}
