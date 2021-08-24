package com.imooc.api.controller.admin;

import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "管理员admin维护", tags = {"管理员admin维护的controller"})
@RequestMapping("adminMng")
public interface AdminMngControllerApi {

    @ApiOperation(value = "hello方法的接口", notes = "hello方法的接口", httpMethod = "POST")
    @PostMapping("/adminLogin")
    public GraceJSONResult adminLogin(@RequestBody AdminLoginBO adminLoginBO,
                                      HttpServletRequest request,
                                      HttpServletResponse response);


}