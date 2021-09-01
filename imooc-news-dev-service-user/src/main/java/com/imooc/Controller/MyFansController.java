package com.imooc.Controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.MyFansControllerApi;
import com.imooc.enums.Sex;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.pojo.vo.FansCountsVO;
import com.imooc.user.service.MyFansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liujq
 * @create 2021-09-01 17:23
 */
@RestController
public class MyFansController extends BaseController implements MyFansControllerApi {

    @Autowired
    private MyFansService myFansService;

    @Override
    public GraceJSONResult isMeFollowThisWriter(String writerId, String fanId) {
        Boolean result = myFansService.isMeFollowThisWriter(writerId, fanId);
        return GraceJSONResult.ok(result);
    }

    @Override
    public GraceJSONResult follow(String writerId, String fanId) {
        myFansService.follow(writerId, fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult unfollow(String writerId, String fanId) {
        myFansService.unfollow(writerId, fanId);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryAll(String writerId, Integer page, Integer pageSize) {
        return GraceJSONResult.ok(myFansService.queryAll(writerId, page, pageSize));
    }

    @Override
    public GraceJSONResult queryRatio(String writerId) {
        int manCounts = myFansService.queryFansCounts(writerId, Sex.man.type);
        int womanCounts = myFansService.queryFansCounts(writerId, Sex.woman.type);
        FansCountsVO fansCountsVO = new FansCountsVO();
        fansCountsVO.setManCounts(manCounts);
        fansCountsVO.setWomanCounts(womanCounts);
        return GraceJSONResult.ok(fansCountsVO);
    }

    @Override
    public GraceJSONResult queryRatioByRegion(String writerId) {
        return GraceJSONResult.ok(myFansService.queryRatioByRegion(writerId));
    }
}
