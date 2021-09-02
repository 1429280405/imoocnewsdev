package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.IPUtil;
import com.imooc.utils.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liujq
 * @create 2021-08-19 9:31
 */
public class ArticleReadInterceptor extends BaseInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisOperator redis;

    public static final String REDIS_ALREADY_READ = "redis_already_read";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String articleId = request.getParameter("articleId");
        String userIp = IPUtil.getRequestIp(request);
        boolean exist = redis.keyIsExist(REDIS_ALREADY_READ + ":" + articleId + ":" + userIp);
        if (exist) {
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
