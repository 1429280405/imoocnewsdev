package com.imooc.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author liujinqiang
 * @create 2021-08-22 23:07
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
public class FilesApp {
    public static void main(String[] args) {
        SpringApplication.run(FilesApp.class,args);
    }
}
