package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.CommentsMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.article.service.CommentService;
import com.imooc.pojo.Comments;
import com.imooc.pojo.bo.CommentReplyBO;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.pojo.vo.CommentsVO;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

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

    @Override
    public PagedGridResult queryArticleComments(String articleId, Integer page, Integer pageSize) {
        HashMap<String, String> map = new HashMap<>();
        map.put("articleId", articleId);
        PageHelper.startPage(page, pageSize);
        List<CommentsVO> commentsList = commentsMapper.queryArticleComments(map);
        PagedGridResult gridResult = setterPagedGrid(commentsList, page);
        return gridResult;
    }

    @Override
    public PagedGridResult queryWriterCommentsMng(String writerId, Integer page, Integer pageSize) {
        Comments comments = new Comments();
        comments.setWriterId(writerId);
        PageHelper.startPage(page, pageSize);
        List<Comments> list = commentsMapper.select(comments);
        return setterPagedGrid(list, page);
    }

    @Override
    public void deleteComment(String writerId, String commentId) {
        Comments comments = new Comments();
        comments.setWriterId(writerId);
        comments.setId(commentId);
        commentsMapper.delete(comments);
    }
}
