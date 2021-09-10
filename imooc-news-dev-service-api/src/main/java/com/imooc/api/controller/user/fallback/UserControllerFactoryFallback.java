package com.imooc.api.controller.user.fallback;

import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liujq
 * @create 2021-09-10 16:36
 */
@Component
public class UserControllerFactoryFallback implements FallbackFactory<UserControllerApi> {
    @Override
    public UserControllerApi create(Throwable throwable) {
        return new UserControllerApi() {
            @Override
            public GraceJSONResult getUserInfo(String userId) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }

            @Override
            public GraceJSONResult getAccountInfo(String userId) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }

            @Override
            public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO userInfoBO) {
                return GraceJSONResult.errorCustom(ResponseStatusEnum.SYSTEM_ERROR);
            }

            @Override
            public GraceJSONResult queryByIds(String userIds) {
                System.out.println("进入客户端（服务调用者）的降级方法");
                List<AppUserVO> publisherList = new ArrayList<>();
                return GraceJSONResult.ok(publisherList);
            }
        };
    }
}