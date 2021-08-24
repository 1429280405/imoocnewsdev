package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;

/**
 * @author liujq
 * @create 2021-08-24 16:27
 */
public interface AdminService {
    AdminUser findByUserName(String username);
}
