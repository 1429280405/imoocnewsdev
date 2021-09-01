package com.imooc.user.service.impl;

import com.imooc.api.service.BaseService;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fans;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.user.service.UserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author liujq
 * @create 2021-09-01 17:26
 */
@Service
public class MyFansServiceImpl extends BaseService implements MyFansService {

    @Autowired
    private UserService userService;

    @Autowired
    private Sid sid;

    @Autowired
    private FansMapper fansMapper;

    @Override
    @Transactional
    public void follow(String writerId, String fanId) {
        AppUser fanInfo = userService.getUser(fanId);
        String fanPkId = sid.nextShort();
        Fans fans = new Fans();
        fans.setId(fanPkId);
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        fans.setFace(fanInfo.getFace());
        fans.setFanNickname(fanInfo.getNickname());
        fans.setSex(fanInfo.getSex());
        fans.setProvince(fanInfo.getProvince());

        fansMapper.insert(fans);
    }
}
