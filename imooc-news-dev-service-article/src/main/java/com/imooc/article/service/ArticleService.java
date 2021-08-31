package com.imooc.article.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.bo.NewArticleBO;
import com.imooc.utils.PagedGridResult;

import java.util.Date;

/**
 * @author liujinqiang
 * @create 2021-08-30 23:03
 */
public interface ArticleService {
    void createArticle(NewArticleBO newArticleBO, Category temp);

    void updateArticleStatus(String id, Integer status);

    void updateAppointToPublish();

    PagedGridResult queryAllList(Integer status, Integer page, Integer pageSize);

    PagedGridResult queryMyArticleList(String userId, String keyword, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);

    void deleteArticle(String articleId, String userId);


    void withdraw(String articleId, String userId);
}
