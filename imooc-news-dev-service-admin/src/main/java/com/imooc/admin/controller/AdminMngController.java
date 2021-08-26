package com.imooc.admin.controller;

import com.imooc.admin.service.AdminService;
import com.imooc.api.BaseController;
import com.imooc.api.controller.admin.AdminMngControllerApi;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.AdminLoginBO;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import com.imooc.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

/**
 * @author liujq
 * @create 2021-08-24 16:49
 */
@RestController
@Slf4j
public class AdminMngController extends BaseController implements AdminMngControllerApi {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult adminLogin(AdminLoginBO adminLoginBO, HttpServletRequest request, HttpServletResponse response) {
        //1、判断请求参数是否为空
        if (StringUtils.isBlank(adminLoginBO.getUsername()) ||
                StringUtils.isBlank(adminLoginBO.getPassword())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //2、通过用户名查询对应管理用户
        AdminUser adminUser = adminService.findByUserName(adminLoginBO.getUsername());
        //3、判断用户是否存在
        if (adminUser == null) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_NOT_EXIT_ERROR);
        }
        //4、判断密码是否匹配
        boolean checkpw = BCrypt.checkpw(adminLoginBO.getPassword(), adminUser.getPassword());
        if (!checkpw) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
        }
        doLoginSettings(adminUser, request, response);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminIsExist(String username) {
        this.checkAdminExist(username);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult getAdminList(Integer page, Integer pageSize) {
        PagedGridResult result = adminService.queryAdminList(page, pageSize);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult addNewAdmin(@Valid NewAdminBO newAdminBO, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
        }
        if (StringUtils.isBlank(newAdminBO.getImg64())) {
            if (StringUtils.isBlank(newAdminBO.getPassword()) ||
                    StringUtils.isBlank(newAdminBO.getConfirmPassword())
            ) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_NULL_ERROR);
            }
        }
        //密码不为空，判断两次输入一致
        if (StringUtils.isNoneBlank(newAdminBO.getPassword())) {
            if (!newAdminBO.getPassword().equals(newAdminBO.getConfirmPassword())) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.ADMIN_PASSWORD_ERROR);
            }
        }
        //校验用户名唯一
        checkAdminExist(newAdminBO.getUsername());
        //调用service存入admin信息
        adminService.createAdminUser(newAdminBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult adminLogout(String adminId, HttpServletRequest request, HttpServletResponse response) {
        //删除redis
        redis.del(REDIS_ADMIN_TOKEN+":"+adminId);
        //删除cookie
        deleteCookie(request,response,"atoken");
        deleteCookie(request,response,"aid");
        deleteCookie(request,response,"aname");
        return GraceJSONResult.ok();
    }


    private void doLoginSettings(AdminUser adminUser, HttpServletRequest request, HttpServletResponse response) {
        //生成token，设置cookie
        String token = UUID.randomUUID().toString();
        redis.set(REDIS_ADMIN_TOKEN + ":" + adminUser.getId(), token);
        setCookie(request, response, "atoken", token, COOKIE_MONTH);
        setCookie(request, response, "aid", adminUser.getId(), COOKIE_MONTH);
        setCookie(request, response, "aname", adminUser.getAdminName(), COOKIE_MONTH);

    }

    private void checkAdminExist(String username) {
        AdminUser admin = adminService.findByUserName(username);

        if (admin != null) {
            GraceException.display(ResponseStatusEnum.ADMIN_USERNAME_EXIST_ERROR);
        }
    }
}
