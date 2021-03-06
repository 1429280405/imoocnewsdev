package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liujq
 * @create 2021-08-17 15:27
 */
@SpringBootApplication(exclude = MongoAutoConfiguration.class)
@MapperScan("com.imooc.user.mapper")
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
@EnableEurekaClient     // 开启eureka client，注册到server中
@EnableCircuitBreaker   // 开启hystrix的熔断机制
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class,args);
    }
}
