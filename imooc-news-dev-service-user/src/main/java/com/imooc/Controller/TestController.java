package com.imooc.Controller;

import com.imooc.api.controller.user.HelloControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujq
 * @create 2021-08-17 15:31
 */
@RestController
@Slf4j
public class TestController implements HelloControllerApi {

    @Autowired
    private RedisOperator redis;

    public Object hello(){
        log.info("请求进来了：{}","123");
        return GraceJSONResult.ok("wordl...");
    }

    @RequestMapping("/redis")
    public GraceJSONResult redis(){
        redis.setnx("name","liujinqiang");
        return GraceJSONResult.ok(redis.get("name"));
    }
}
