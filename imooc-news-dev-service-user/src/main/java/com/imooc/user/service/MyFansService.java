package com.imooc.user.service;

import com.imooc.enums.Sex;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

/**
 * @author liujq
 * @create 2021-09-01 17:26
 */
public interface MyFansService {
    void follow(String writerId, String fanId);

    Boolean isMeFollowThisWriter(String writerId, String fanId);

    void unfollow(String writerId, String fanId);

    PagedGridResult queryAll(String writerId, Integer page, Integer pageSize);

    int queryFansCounts(String writerId, Integer sex);

    List<RegionRatioVO> queryRatioByRegion(String writerId);
}
