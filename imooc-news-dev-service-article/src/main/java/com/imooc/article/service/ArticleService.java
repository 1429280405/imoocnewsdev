package com.imooc.article.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;

/**
 * @author liujinqiang
 * @create 2021-08-30 23:03
 */
public interface ArticleService {
    void createArticle(NewArticleBO newArticleBO, Category temp);
}
