package com.imooc.article.service.impl;

import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author liujinqiang
 * @create 2021-08-30 23:03
 */
@Service
public class ArticleServiceImpl extends BaseService implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private Sid sid;

    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category temp) {
        Article article = new Article();
        BeanUtils.copyProperties(newArticleBO, article);
        //设置id
        article.setId(sid.nextShort());
        article.setCategoryId(temp.getId());
        //设置相关初始状态
        article.setArticleStatus(ArticleReviewStatus.REVIEWING.type);
        article.setCommentCounts(0);
        article.setReadCounts(0);
        article.setIsDelete(YesOrNo.NO.type);
        //设置相关时间
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        if (article.getIsAppoint() == ArticleAppointType.TIMING.type) {
            article.setPublishTime(newArticleBO.getPublishTime());
        } else {
            article.setPublishTime(new Date());
        }

        //保存入库
        int result = articleMapper.insert(article);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_CREATE_ERROR);
        }
    }
}
