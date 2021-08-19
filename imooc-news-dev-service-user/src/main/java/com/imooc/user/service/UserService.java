package com.imooc.user.service;

import com.imooc.pojo.AppUser;

/**
 * @author liujq
 * @create 2021-08-19 15:23
 */
public interface UserService {
    /**
     * 判断用户是否存在，如果存在返回user信息
     */
    public AppUser queryMobileIsExist(String mobile);

    /**
     * 创建用户，新增用户记录到数据库
     */
    public AppUser createUser(String mobile);
}
