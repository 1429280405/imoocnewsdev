package com.imooc.api.controller.user;

import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujq
 * @create 2021-08-17 16:06
 */
public interface HelloControllerApi {

    @RequestMapping("/hello")
    public Object hello();
}
