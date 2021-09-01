package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author liujinqiang
 * @create 2021-08-17 23:01
 */
@Api(value = "粉丝管理", tags = {"粉丝管理Controller"})
@RequestMapping("fans")
public interface MyFansControllerApi {

    @ApiOperation(value = "用户关注作家", notes = "用户关注作家", httpMethod = "POST")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String writerId,
                                  @RequestParam String fanId);

}
