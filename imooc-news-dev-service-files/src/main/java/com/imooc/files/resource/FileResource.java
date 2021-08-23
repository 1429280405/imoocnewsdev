package com.imooc.files.resource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author liujinqiang
 * @create 2021-08-23 19:59
 */
@Component
@ConfigurationProperties(prefix = "file")
@PropertySource("classpath:file-${spring.profiles.active}.properties")
@Data
public class FileResource {
    private String host;
}
