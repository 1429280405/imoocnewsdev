package com.imooc.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author liujinqiang
 * @create 2021-08-22 23:07
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FilesApp {
    public static void main(String[] args) {
        SpringApplication.run(FilesApp.class,args);
    }
}
