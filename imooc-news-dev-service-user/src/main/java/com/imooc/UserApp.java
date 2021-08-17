package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author liujq
 * @create 2021-08-17 15:27
 */
@SpringBootApplication
@MapperScan("com.imooc.user.mapper")
public class UserApp {
    public static void main(String[] args) {
        SpringApplication.run(UserApp.class,args);
    }
}
