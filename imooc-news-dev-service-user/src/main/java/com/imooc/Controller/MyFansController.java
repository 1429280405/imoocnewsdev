package com.imooc.Controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.user.service.MyFansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujq
 * @create 2021-09-01 17:23
 */
@RestController
public class MyFansController  extends BaseController implements MyFansControllerApi {

    @Autowired
    private MyFansService myFansService;

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId,fanId);
        return GraceJSONResult.ok();
    }
}
