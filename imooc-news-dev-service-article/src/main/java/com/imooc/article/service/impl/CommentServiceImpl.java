package com.imooc.article.service.impl;

import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.CommentService;
import com.imooc.pojo.Comments;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author liujinqiang
 * @create 2021-09-02 21:11
 */
@Service
public class CommentServiceImpl extends BaseService implements CommentService {

    @Autowired
    private CommentsMapper commentsMapper;

    @Autowired
    private Sid sid;

    @Autowired
    private ArticlePortalService articlePortalService;

    @Transactional
    @Override
    public void createComment(CommentReplyBO commentReplyBO, String nickname) {
        Comments comments = new Comments();
        String commentId = sid.nextShort();
        ArticleDetailVO article = articlePortalService.queryDetail(commentReplyBO.getArticleId());
        comments.setId(commentId);
        comments.setWriterId(article.getPublishUserId());
        comments.setArticleTitle(article.getTitle());
        comments.setArticleCover(article.getCover());
        comments.setArticleId(article.getId());
        comments.setFatherId(commentReplyBO.getFatherId());
        comments.setCommentUserId(commentReplyBO.getCommentUserId());
        comments.setCommentUserNickname(nickname);
        comments.setContent(commentReplyBO.getContent());
        comments.setCreateTime(new Date());

        commentsMapper.insert(comments);
        //评论数累加
        redis.increment(REDIS_ARTICLE_COMMENT_COUNTS + ":" + article.getId(), 1);
    }
}
