package com.imooc.api.interceptors;

import com.imooc.enums.UserStatus;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * * 用户激活状态检查拦截器
 * * 发文章，修改文章，删除文章，
 * * 发表评论，查看评论等等
 * * 这些接口都是需要在用户激活以后，才能进行
 * * 否则需要提示用户前往[账号设置]去修改信息
 *
 * @author liujq
 * @create 2021-08-19 9:31
 */
public class UserActiveInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String userId = request.getHeader("headerUserId");
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        if (StringUtils.isBlank(userJson)) {
            GraceException.display(ResponseStatusEnum.UN_LOGIN);
            return false;
        }
        AppUser user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        if (user.getActiveStatus() == null || user.getActiveStatus() != UserStatus.ACTIVE.type) {
            GraceException.display(ResponseStatusEnum.USER_INACTIVE_ERROR);
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
