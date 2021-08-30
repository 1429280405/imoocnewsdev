package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.enums.UserStatus;
import com.imooc.pojo.AppUser;
import com.imooc.user.mapper.AppUserMapper;
import com.imooc.user.service.AppUserMngService;
import com.imooc.utils.PagedGridResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * @author liujq
 * @create 2021-08-30 14:58
 */
@Service
public class AppUserMngServiceImpl extends BaseService implements AppUserMngService {

    @Autowired
    private AppUserMapper userMapper;

    @Override
    public PagedGridResult queryAll(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize) {
        Example example = new Example(AppUser.class);
        example.orderBy("createdTime").desc();
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNoneBlank(nickname)) {
            criteria.andEqualTo("nickname", nickname);
        }
        if (UserStatus.isUserStatusValid(status)) {
            criteria.andEqualTo("status", status);
        }
        if (startDate != null) {
            criteria.andGreaterThanOrEqualTo("createdTime", startDate);
        }
        if (endDate != null) {
            criteria.andLessThanOrEqualTo("createdTime", endDate);
        }
        PageHelper.startPage(page, pageSize);
        List<AppUser> appUsers = userMapper.selectByExample(example);
        PagedGridResult gridResult = setterPagedGrid(appUsers, page);
        return gridResult;
    }

    @Transactional
    @Override
    public void freezeUserOrNot(String userId, Integer doStatus) {
        AppUser user = new AppUser();
        user.setId(userId);
        user.setActiveStatus(doStatus);
        userMapper.updateByPrimaryKeySelective(user);
    }
}
