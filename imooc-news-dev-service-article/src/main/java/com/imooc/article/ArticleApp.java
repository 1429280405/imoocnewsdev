package com.imooc.article;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.CircuitBreaker;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liujq
 * @create 2021-08-30 17:52
 */
@SpringBootApplication
@MapperScan(basePackages = "com.imooc.article.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
@EnableEurekaClient     // 开启eureka client，注册到server中
@EnableFeignClients({"com.imooc"})
@EnableHystrix
public class ArticleApp {
    public static void main(String[] args) {
        SpringApplication.run(ArticleApp.class, args);
    }
}
