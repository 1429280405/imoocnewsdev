package com.imooc.article.service;

import com.imooc.pojo.Article;
import com.imooc.pojo.vo.ArticleDetailVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @author liujq
 * @create 2021-09-01 10:46
 */
public interface ArticlePortalService {
    PagedGridResult queryIndexArticleList(String keyword, Integer category, Integer page, Integer pageSize);

    List<Article> hotList();

    ArticleDetailVO queryDetail(String articleId);
}
