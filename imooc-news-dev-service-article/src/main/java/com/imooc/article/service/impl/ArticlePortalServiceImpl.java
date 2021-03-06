package com.imooc.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.article.mapper.ArticleMapper;
import com.imooc.article.service.ArticlePortalService;
import com.imooc.enums.ArticleReviewStatus;
import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author liujq
 * @create 2021-09-01 10:46
 */
@Service
public class ArticlePortalServiceImpl extends BaseService implements ArticlePortalService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize) {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = generateCriteria(articleExample);

        if (StringUtils.isNotBlank(keyword)) {
            criteria.andLike("title", "%" + keyword + "%");
        }
        if (category != null) {
            criteria.andEqualTo("categoryId", category);
        }

        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.selectByExample(articleExample);

        return setterPagedGrid(list, page);
    }

    @Override
    public List<Article> hotList() {
        Example articleExample = new Example(Article.class);
        Example.Criteria criteria = generateCriteria(articleExample);
        PageHelper.startPage(1, 5);
        List<Article> list = articleMapper.selectByExample(articleExample);
        return list;
    }

    @Override
    public ArticleDetailVO queryDetail(String articleId) {
        Article article = new Article();
        article.setId(articleId);
        article.setIsAppoint(YesOrNo.NO.type);
        article.setIsDelete(YesOrNo.NO.type);
        article.setArticleStatus(ArticleReviewStatus.SUCCESS.type);
        Article result = articleMapper.selectOne(article);

        ArticleDetailVO detailVO = new ArticleDetailVO();
        BeanUtils.copyProperties(result, detailVO);
        detailVO.setCover(result.getArticleCover());
        return detailVO;
    }

    private Example.Criteria generateCriteria(Example articleExample) {
        articleExample.orderBy("publishTime").desc();
        Example.Criteria criteria = articleExample.createCriteria();

        /**
         * ????????????????????????????????????????????????
         * isAppoint=????????????????????????????????????????????????????????????????????????????????????
         * isDelete=????????????????????????????????????????????????
         * articleStatus=?????????????????????????????????????????????/??????????????????????????????
         */
        criteria.andEqualTo("isAppoint", YesOrNo.NO.type);
        criteria.andEqualTo("isDelete", YesOrNo.NO.type);
        criteria.andEqualTo("articleStatus", ArticleReviewStatus.SUCCESS.type);
        return criteria;
    }
}
