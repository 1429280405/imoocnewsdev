package com.imooc.admin.service.impl;

import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminService;
import com.imooc.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @author liujq
 * @create 2021-08-24 16:29
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminUserMapper userMapper;

    @Override
    public AdminUser findByUserName(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser admin = userMapper.selectOneByExample(adminExample);
        return admin;
    }
}
