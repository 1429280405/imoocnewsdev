package com.imooc.Controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.PassportControllerApi;
import com.imooc.enums.UserStatus;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.RegistLoginBO;
import com.imooc.user.service.UserService;
import com.imooc.utils.IPUtil;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.SMSUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;

/**
 * @author liujinqiang
 * @create 2021-08-17 23:04
 */
@RestController
@Slf4j
public class PassportController extends BaseController implements PassportControllerApi {

    @Autowired
    private SMSUtils smsUtils;

    @Autowired
    private UserService userService;

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

    @Override
    public GraceJSONResult doLogin(@Valid RegistLoginBO registLoginBO, BindingResult result, HttpServletRequest request, HttpServletResponse response) {
        if (result.hasErrors()) {
            Map<String, String> map = getErrors(result);
            return GraceJSONResult.errorMap(map);
        }
        //校验验证码是否匹配
        String checkCode = redis.get(MOBILE_SMSCODE + ":" + registLoginBO.getMobile());
        if (StringUtils.isEmpty(checkCode) || !checkCode.equals(registLoginBO.getSmsCode())) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.SMS_CODE_ERROR);
        }
        //查询数据库，判断用户是否注册
        AppUser appUser = userService.queryMobileIsExist(registLoginBO.getMobile());
        if (appUser != null && appUser.getActiveStatus() == UserStatus.FROZEN.type) {
            //如果用户不为空并且是已冻结状态，抛出异常禁止登陆
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_FROZEN);
        } else if (appUser == null) {
            //用户未注册入库
            appUser = userService.createUser(registLoginBO.getMobile());
        }

        //保存用户会话
        Integer activeStatus = appUser.getActiveStatus();
        if (activeStatus != UserStatus.FROZEN.type) {
            //保存token到redis
            String uToken = UUID.randomUUID().toString();
            redis.set(REDIS_USER_TOKEN + ":" + appUser.getId(), uToken);
            redis.set(REDIS_USER_INFO + ":" + appUser.getId(), JsonUtils.objectToJson(appUser));

            //保存用户id和token到cookie中
            setCookie(request, response, "utoken", uToken, COOKIE_MONTH);
            setCookie(request, response, "uid", appUser.getId(), COOKIE_MONTH);
        }
        //用户注册成功后需要删除redis短信验证码
        redis.del(MOBILE_SMSCODE + ":" + registLoginBO.getMobile());
        //返回用户状态
        return GraceJSONResult.ok(activeStatus);
    }

    @Override
    public GraceJSONResult logout(String userId, HttpServletRequest request, HttpServletResponse response) {
        //删除redis缓存
        redis.del(REDIS_USER_TOKEN+":"+userId);
        //删除cookie
        setCookie(request,response,"utoken","",COOKIE_DELETE);
        setCookie(request,response,"uid","",COOKIE_DELETE);
        return GraceJSONResult.ok();
    }
}
