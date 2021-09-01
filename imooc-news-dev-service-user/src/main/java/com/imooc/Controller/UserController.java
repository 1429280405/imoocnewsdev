package com.imooc.Controller;

import com.imooc.api.BaseController;
import com.imooc.api.controller.user.UserControllerApi;
import com.imooc.grace.result.GraceJSONResult;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.pojo.AppUser;
import com.imooc.pojo.bo.UpdateUserInfoBO;
import com.imooc.pojo.vo.AppUserVO;
import com.imooc.pojo.vo.UserAccountInfoVO;
import com.imooc.user.service.UserService;
import com.imooc.utils.JsonUtils;
import com.imooc.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liujinqiang
 * @create 2021-08-19 19:28
 */
@RestController
@Slf4j
public class UserController extends BaseController implements UserControllerApi {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisOperator redis;

    @Override
    public GraceJSONResult getUserInfo(String userId) {
        //判断参数不能为空
        if (StringUtils.isEmpty(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        //根据userId查询用户信息
        AppUser user = getUser(userId);
        //返回用户信息
        AppUserVO userVO = new AppUserVO();
        BeanUtils.copyProperties(user, userVO);

        //查询redis中用户的关注数和粉丝数，放入vo进行渲染
        userVO.setMyFansCounts(getCountsFromRedis(REDIS_WRITER_FANS_COUNTS + ":" + userId));
        userVO.setMyFollowCounts(getCountsFromRedis(REDIS_MY_FOLLOW_COUNTS + ":" + userId));
        return GraceJSONResult.ok(userVO);
    }

    private AppUser getUser(String userId) {
        //查询判断redis中是否包含用户信息，如果包含直接返回无需查询数据库
        String userJson = redis.get(REDIS_USER_INFO + ":" + userId);
        AppUser user = null;
        if (StringUtils.isNoneBlank(userJson)) {
            user = JsonUtils.jsonToPojo(userJson, AppUser.class);
        } else {
            user = userService.getUser(userId);
            //存入redis
            redis.set(REDIS_USER_INFO + ":" + userId, JsonUtils.objectToJson(user));
        }
        return user;
    }

    @Override
    public GraceJSONResult getAccountInfo(String userId) {
        //判断参数不能为空
        if (StringUtils.isBlank(userId)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_STATUS_ERROR);
        }
        AppUser user = getUser(userId);
        //返回用户信息
        UserAccountInfoVO accountInfoVO = new UserAccountInfoVO();
        BeanUtils.copyProperties(user, accountInfoVO);
        return GraceJSONResult.ok(accountInfoVO);
    }

    @Override
    public GraceJSONResult updateUserInfo(@Valid UpdateUserInfoBO userInfoBO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> map = getErrors(bindingResult);
            return GraceJSONResult.errorMap(map);
        }
        //执行更新操作
        userService.updateUserInfo(userInfoBO);
        return GraceJSONResult.ok();
    }

    @Override
    public GraceJSONResult queryByIds(String userIds) {
        if (StringUtils.isBlank(userIds)) {
            return GraceJSONResult.errorCustom(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        List<AppUserVO> list = new ArrayList<>();
        List<String> ids = JsonUtils.jsonToList(userIds, String.class);
        for (String id : ids) {
            AppUserVO appUserVO = getBasicUserInfo(id);
            list.add(appUserVO);
        }

        return GraceJSONResult.ok(list);
    }

    private AppUserVO getBasicUserInfo(String id) {
        AppUser user = getUser(id);
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(user, appUserVO);
        return appUserVO;
    }
}
