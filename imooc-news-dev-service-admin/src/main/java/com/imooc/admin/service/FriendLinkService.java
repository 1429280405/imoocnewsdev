package com.imooc.admin.service;

import com.imooc.pojo.mo.FriendLinkMO;

import java.util.List;

/**
 * @author liujinqiang
 * @create 2021-08-29 11:39
 */
public interface FriendLinkService {
    public void saveOrUpdateFriendLink(FriendLinkMO friendLinkMO);

    public List<FriendLinkMO> getFriendLinkList();

    void delete(String linkId);

    List<FriendLinkMO> queryPortalAllFriendLinkList();
}
