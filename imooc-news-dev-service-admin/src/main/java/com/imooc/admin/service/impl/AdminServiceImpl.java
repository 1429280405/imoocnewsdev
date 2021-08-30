package com.imooc.admin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.imooc.admin.mapper.AdminUserMapper;
import com.imooc.admin.service.AdminService;
import com.imooc.api.service.BaseService;
import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AdminUser;
import com.imooc.pojo.bo.NewAdminBO;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liujq
 * @create 2021-08-24 16:29
 */
@Service
public class AdminServiceImpl extends BaseService implements AdminService {

    @Autowired
    private AdminUserMapper userMapper;

    @Autowired
    private Sid sid;

    @Override
    public AdminUser findByUserName(String username) {
        Example adminExample = new Example(AdminUser.class);
        Example.Criteria criteria = adminExample.createCriteria();
        criteria.andEqualTo("username", username);

        AdminUser admin = userMapper.selectOneByExample(adminExample);
        return admin;
    }

    @Override
    public PagedGridResult queryAdminList(Integer page, Integer pageSize) {
        Example example = new Example(AdminUser.class);
        example.orderBy("createdTime").desc();
        PageHelper.startPage(page, pageSize);
        List<AdminUser> userList = userMapper.selectByExample(example);
        return setterPagedGrid(userList, page);
    }

    @Transactional
    @Override
    public void createAdminUser(NewAdminBO newAdminBO) {
        String adminId = sid.nextShort();
        AdminUser user = new AdminUser();
        user.setId(adminId);
        user.setUsername(newAdminBO.getUsername());
        user.setAdminName(newAdminBO.getAdminName());
        //如果密码不为空，需要加密入库
        if (StringUtils.isNoneBlank(newAdminBO.getPassword())) {
            String pwd = BCrypt.hashpw(newAdminBO.getPassword(), BCrypt.gensalt());
            user.setPassword(pwd);
        }
        //如果人脸上传以后有faceId，需要和admin信息关联入库
        if (StringUtils.isNoneBlank(newAdminBO.getFaceId())) {
            user.setFaceId(newAdminBO.getFaceId());
        }
        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());

        int result = userMapper.insert(user);
        if (result != 1) {
            GraceException.display(ResponseStatusEnum.ADMIN_CREATE_ERROR);
        }

    }


}
