package com.imooc.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liujq
 * @create 2021-08-30 17:52
 */
@SpringBootApplication
@MapperScan(basePackages = "com.imooc.article.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
public class ArticleApp {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApp.class, args);
    }
}
