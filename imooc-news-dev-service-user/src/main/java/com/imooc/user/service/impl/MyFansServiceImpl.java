package com.imooc.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.imooc.api.service.BaseService;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.Fans;
import com.imooc.pojo.vo.RegionRatioVO;
import com.imooc.user.mapper.FansMapper;
import com.imooc.user.service.MyFansService;
import com.imooc.user.service.UserService;
import com.imooc.utils.PagedGridResult;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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

    public static final String[] regions = {"北京", "天津", "上海", "重庆",
            "河北", "山西", "辽宁", "吉林", "黑龙江", "江苏", "浙江", "安徽", "福建", "江西", "山东",
            "河南", "湖北", "湖南", "广东", "海南", "四川", "贵州", "云南", "陕西", "甘肃", "青海", "台湾",
            "内蒙古", "广西", "西藏", "宁夏", "新疆",
            "香港", "澳门"};

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

        //redis作者粉丝数增加
        redis.increment(REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        //当前用户关注数增加
        redis.increment(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);
    }

    @Override
    public Boolean isMeFollowThisWriter(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setFanId(fanId);
        fans.setWriterId(writerId);
        int count = fansMapper.selectCount(fans);

        return count > 0 ? true : false;
    }

    @Override
    public void unfollow(String writerId, String fanId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setFanId(fanId);
        fansMapper.delete(fans);

        //redis作者粉丝数减少
        redis.decrement(REDIS_WRITER_FANS_COUNTS + ":" + writerId, 1);
        //当前用户关注数减少
        redis.decrement(REDIS_MY_FOLLOW_COUNTS + ":" + fanId, 1);

    }

    @Override
    public PagedGridResult queryAll(String writerId, Integer page, Integer pageSize) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        PageHelper.startPage(page, pageSize);
        List<Fans> list = fansMapper.select(fans);
        return setterPagedGrid(list, page);
    }

    @Override
    public int queryFansCounts(String writerId, Integer sex) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        fans.setSex(sex);
        return fansMapper.selectCount(fans);
    }

    @Override
    public List<RegionRatioVO> queryRatioByRegion(String writerId) {
        Fans fans = new Fans();
        fans.setWriterId(writerId);
        List<RegionRatioVO> regionRatioVOList = new ArrayList<>();
        for (String region : regions) {
            fans.setProvince(region);
            int count = fansMapper.selectCount(fans);
            RegionRatioVO regionRatioVO = new RegionRatioVO();
            regionRatioVO.setName(region);
            regionRatioVO.setValue(count);
            regionRatioVOList.add(regionRatioVO);
        }
        return regionRatioVOList;
    }
}
