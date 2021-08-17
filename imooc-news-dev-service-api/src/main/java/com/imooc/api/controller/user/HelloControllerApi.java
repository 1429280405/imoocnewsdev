package com.imooc.api.controller.user;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liujq
 * @create 2021-08-17 16:06
 */
@Api(value = "controller的标题", tags = {"xx功能的controller"})
public interface HelloControllerApi {

    @ApiOperation(value = "hello方法的接口",notes = "hello方法的接口",httpMethod = "GET")
    @RequestMapping("/hello")
    public Object hello();
}
