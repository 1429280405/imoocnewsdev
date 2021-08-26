package com.imooc.api.config;

import com.imooc.api.interceptors.AdminCookieAndTokenInterceptor;
import com.imooc.api.interceptors.PassportInterceptor;
import com.imooc.api.interceptors.UserActiveInterceptor;
import com.imooc.api.interceptors.UserTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author liujq
 * @create 2021-08-19 10:12
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    public PassportInterceptor passportInterceptor() {
        return new PassportInterceptor();
    }

    @Bean
    public UserTokenInterceptor userTokenInterceptor() {
        return new UserTokenInterceptor();
    }

    @Bean
    public UserActiveInterceptor userActiveInterceptor() {
        return new UserActiveInterceptor();
    }

    @Bean
    public AdminCookieAndTokenInterceptor adminCookieAndTokenInterceptor(){
        return new AdminCookieAndTokenInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(passportInterceptor())
                .addPathPatterns("/passport/getSMSCode");

        registry.addInterceptor(userTokenInterceptor())
                .addPathPatterns("/user/getAccountInfo")
                .addPathPatterns("/user/updateUserInfo")
                .addPathPatterns("/fs/uploadFace");

        registry.addInterceptor(userActiveInterceptor())
                .addPathPatterns("/fs/uploadSomeFiles")
                .addPathPatterns("/fans/follow")
                .addPathPatterns("/fans/unfollow");


        registry.addInterceptor(adminCookieAndTokenInterceptor())
                .addPathPatterns("/adminMng/adminIsExist")
                .addPathPatterns("/adminMng/addNewAdmin")
                .addPathPatterns("/adminMng/getAdminList")
                .addPathPatterns("/fs/uploadToGridFS")
                .addPathPatterns("/fs/readInGridFS")
                .addPathPatterns("/friendLinkMng/saveOrUpdateFriendLink")
                .addPathPatterns("/friendLinkMng/getFriendLinkList")
                .addPathPatterns("/friendLinkMng/delete")
                .addPathPatterns("/categoryMng/saveOrUpdateCategory")
                .addPathPatterns("/categoryMng/getCatList");
    }
}
