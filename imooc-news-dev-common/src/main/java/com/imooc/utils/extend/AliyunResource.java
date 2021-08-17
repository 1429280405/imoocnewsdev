package com.imooc.utils.extend;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author liujinqiang
 * @create 2021-08-17 22:09
 */
@Component
@PropertySource("aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
@Data
public class AliyunResource {
    private String accessKeyId;
    private String accessKeySecret;
}
