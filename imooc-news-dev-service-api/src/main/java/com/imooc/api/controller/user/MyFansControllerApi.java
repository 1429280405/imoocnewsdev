package com.imooc.api.controller.user;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

    @ApiOperation(value = "查询当前用户是否关注作家", notes = "查询当前用户是否关注作家", httpMethod = "POST")
    @PostMapping("/isMeFollowThisWriter")
    public GraceJSONResult isMeFollowThisWriter(@RequestParam String writerId,
                                  @RequestParam String fanId);

    @ApiOperation(value = "用户关注作家", notes = "用户关注作家", httpMethod = "POST")
    @PostMapping("/follow")
    public GraceJSONResult follow(@RequestParam String writerId,
                                  @RequestParam String fanId);

    @ApiOperation(value = "取消关注作家", notes = "取消关注作家", httpMethod = "POST")
    @PostMapping("/unfollow")
    public GraceJSONResult unfollow(@RequestParam String writerId,
                                                @RequestParam String fanId);

    @ApiOperation(value = "查询我的所有粉丝列表", notes = "查询我的所有粉丝列表", httpMethod = "POST")
    @PostMapping("/queryAll")
    public GraceJSONResult queryAll(@RequestParam String writerId,
                                    @ApiParam(name = "page", value = "查询下一页的第几页", required = false,defaultValue = "1")
                                        @RequestParam Integer page,
                                    @ApiParam(name = "pageSize", value = "分页查询每一页显示的条数", required = false,defaultValue = "10")
                                        @RequestParam Integer pageSize);

    @ApiOperation(value = "查询粉丝画像", notes = "查询粉丝画像", httpMethod = "POST")
    @PostMapping("/queryRatio")
    public GraceJSONResult queryRatio(@RequestParam String writerId);

    @ApiOperation(value = "根据地域查询粉丝画像", notes = "根据地域查询粉丝画像", httpMethod = "POST")
    @PostMapping("/queryRatioByRegion")
    public GraceJSONResult queryRatioByRegion(@RequestParam String writerId);


}
