package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liujinqiang
 * @create 2021-08-17 23:01
 */
@Api(value = "用户登录注册",tags = {"用户登录注册的controller"})
public interface PassportControllerApi {

    @ApiOperation(value = "获得短信验证码",notes = "获得短信验证码",httpMethod = "GET")
    @GetMapping("/getSMSCode")
    public GraceJSONResult getSMSCode(@RequestParam String mobile, HttpServletRequest request);
}
