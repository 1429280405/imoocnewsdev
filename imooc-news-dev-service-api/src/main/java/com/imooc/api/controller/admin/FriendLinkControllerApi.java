package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.pojo.bo.SaveFriendLinkBO;
import com.imooc.pojo.mo.FriendLinkMO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @author liujq
 * @create 2021-08-24 16:44
 */
@Api(value = "首页友情链接维护", tags = {"首页友情链接维护controller"})
@RequestMapping("friendLinkMng")
public interface FriendLinkControllerApi {

    @ApiOperation(value = "新增或修改友情链接", notes = "新增或修改友情链接", httpMethod = "POST")
    @PostMapping("/saveOrUpdateFriendLink")
    public GraceJSONResult saveOrUpdateFriendLink(@RequestBody @Valid SaveFriendLinkBO saveFriendLinkBO,
                                                  BindingResult result);

    @ApiOperation(value = "查询友情链接", notes = "查询友情链接", httpMethod = "POST")
    @PostMapping("/getFriendLinkList")
    public GraceJSONResult getFriendLinkList();

    @ApiOperation(value = "删除友情链接", notes = "删除友情链接", httpMethod = "POST")
    @PostMapping("/delete")
    public GraceJSONResult delete(@RequestParam String linkId);


}