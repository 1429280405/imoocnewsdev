package com.imooc.Controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.utils.IPUtil;
import com.imooc.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liujinqiang
 * @create 2021-08-17 23:04
 */
@RestController
@Slf4j
public class PassportController extends BaseController implements PassportControllerApi {

    @Autowired
    private SMSUtils smsUtils;


    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        //获的用户ip
        String userIp = IPUtil.getRequestIp(request);
        //根据用户ip进行限制，限制用户在60s内只能获得一次验证码
        redis.setnx60s(MOBILE_SMSCODE + ":" + userIp, userIp);
        //生成随机验证码并且发送短信
        String random = (int) ((Math.random() * 9 + 1) * 100000) + "";
        //把验证码存入redis，用于后续验证
        redis.set(MOBILE_SMSCODE + ":" + mobile, random, 30 * 60);
        return GraceJSONResult.ok();
    }
}
