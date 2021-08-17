package com.imooc.Controller;

import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.grace.result.GraceJSONResult;
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
public class PassportController implements PassportControllerApi {

    @Autowired
    private SMSUtils smsUtils;

    @Override
    public GraceJSONResult getSMSCode(String mobile, HttpServletRequest request) {
        smsUtils.sendSMS(mobile,"159485");
        return GraceJSONResult.ok();
    }
}
