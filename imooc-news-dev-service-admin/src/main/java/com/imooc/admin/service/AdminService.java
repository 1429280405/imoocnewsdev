package com.imooc.admin.service;

import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;

/**
 * @author liujq
 * @create 2021-08-24 16:27
 */
public interface AdminService {
    AdminUser findByUserName(String username);

    PagedGridResult queryAdminList(Integer page, Integer pageSize);

    void createAdminUser(NewAdminBO newAdminBO);
}
