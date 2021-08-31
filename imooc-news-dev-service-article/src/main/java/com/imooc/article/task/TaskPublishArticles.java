package com.imooc.article.task;

import com.imooc.article.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author liujq
 * @create 2021-08-31 10:33
 */
@Configuration
@EnableScheduling //开启定时任务
public class TaskPublishArticles {

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0/3 * * * * ?")
    public void publishArticles(){
        //调用文章service，把当前时间应该发布的定时文章，状态改为即时
        articleService.updateAppointToPublish();
    }
}
