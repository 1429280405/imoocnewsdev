package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.mapper.ArticleMapperCustom;
import com.imooc.article.service.ArticleService;
import com.imooc.enums.ArticleAppointType;
import com.imooc.enums.ArticleReviewLevel;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.Article;
import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Id;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.applet.AudioClip;
import java.util.Date;
import java.util.List;

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

    @Autowired
    private ArticleMapperCustom articleMapperCustom;

    @Transactional
    @Override
    public void createArticle(NewArticleBO newArticleBO, Category temp) {
        Article article = new Article();
        BeanUtils.copyProperties(newArticleBO, article);
        //设置id
        String articleId = sid.nextShort();
        article.setId(articleId);
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
        // 通过阿里智能AI实现对文章文本的自动检测（自动审核）
        //        String reviewTextResult = aliTextReviewUtils.reviewTextContent(newArticleBO.getContent());
        String reviewTextResult = ArticleReviewLevel.REVIEW.type;
        if (reviewTextResult.equalsIgnoreCase(ArticleReviewLevel.PASS.type)) {
            this.updateArticleStatus(articleId, ArticleReviewStatus.SUCCESS.type);
        } else if (reviewTextResult
                .equalsIgnoreCase(ArticleReviewLevel.REVIEW.type)) {
            // 修改当前的文章，状态标记为需要人工审核
            this.updateArticleStatus(articleId, ArticleReviewStatus.WAITING_MANUAL.type);
        } else if (reviewTextResult
                .equalsIgnoreCase(ArticleReviewLevel.BLOCK.type)) {
            // 修改当前的文章，状态标记为审核未通过
            this.updateArticleStatus(articleId, ArticleReviewStatus.FAILED.type);
        }
    }

    @Override
    public void updateArticleStatus(String id, Integer status) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", id);
        Article article = new Article();
        article.setArticleStatus(status);
        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_REVIEW_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateAppointToPublish() {
        articleMapperCustom.updateAppointToPublish();
    }

    @Override
    public PagedGridResult queryAllList(Integer status, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }
        //审核中是机审和人申两个状态
        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL);
        }
        //必须是未删除的
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        PagedGridResult gridResult = setterPagedGrid(articles, page);

        return gridResult;
    }

    @Override
    public PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(Article.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("publishUserId", userId);

        if (StringUtils.isNoneBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (ArticleReviewStatus.isArticleStatusValid(status)) {
            criteria.andEqualTo("articleStatus", status);
        }

        if (status != null && status == 12) {
            criteria.andEqualTo("articleStatus", ArticleReviewStatus.REVIEWING.type)
                    .orEqualTo("articleStatus", ArticleReviewStatus.WAITING_MANUAL.type);
        }

        criteria.andEqualTo("isDelete", YesOrNo.NO.type);

        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("publishTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("publishTime", endDate);
        }
        PageHelper.startPage(page, pageSize);
        List<Article> articles = articleMapper.selectByExample(example);
        return setterPagedGrid(articles, page);
    }

    @Transactional
    @Override
    public void deleteArticle(String articleId, String userId) {
        Example example = generateExample(articleId, userId);
        Article article = new Article();
        article.setIsDelete(YesOrNo.YES.type);
        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_DELETE_ERROR);
        }
    }

    @Override
    public void withdraw(String articleId, String userId) {
        Example example = generateExample(articleId, userId);
        Article article = new Article();
        article.setArticleStatus(ArticleReviewStatus.WITHDRAW.type);
        int result = articleMapper.updateByExampleSelective(article, example);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ARTICLE_WITHDRAW_ERROR);
        }
    }

    @Transactional
    @Override
    public void updateArticleToGridFs(String articleId, String articleMongoId) {
        Article article = new Article();
        article.setId(articleId);
        article.setMongoFileId(articleMongoId);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    private Example generateExample(String articleId, String userId) {
        Example example = new Example(Article.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id", articleId);
        criteria.andEqualTo("publishUserId", userId);
        return example;
    }
}
