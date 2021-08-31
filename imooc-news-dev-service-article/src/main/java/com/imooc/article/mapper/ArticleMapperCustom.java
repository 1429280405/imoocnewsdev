package com.imooc.article.mapper;

import com.imooc.my.mapper.MyMapper;
import com.imooc.pojo.Article;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author liujq
 * @create 2021-08-31 10:54
 */
@Repository
public interface ArticleMapperCustom extends MyMapper<Article> {

    @Update("update article set is_appoint = 0 where publish_time <= NOW() and is_appoint = 1")
    public void updateAppointToPublish();
}
