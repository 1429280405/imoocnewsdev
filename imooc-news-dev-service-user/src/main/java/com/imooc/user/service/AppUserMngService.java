package com.imooc.user.service;

import com.imooc.utils.PagedGridResult;

import java.util.Date;

/**
 * @author liujq
 * @create 2021-08-30 14:58
 */
public interface AppUserMngService {
    PagedGridResult queryAll(String nickname, Integer status, Date startDate, Date endDate, Integer page, Integer pageSize);

    void freezeUserOrNot(String userId, Integer doStatus);
}
