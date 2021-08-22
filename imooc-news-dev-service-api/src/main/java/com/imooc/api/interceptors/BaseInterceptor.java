package com.imooc.api.interceptors;

import com.imooc.exception.GraceException;
import com.imooc.grace.result.ResponseStatusEnum;
import com.imooc.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author liujinqiang
 * @create 2021-08-20 19:13
 */
public class BaseInterceptor {

    @Autowired
    public RedisOperator redis;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_INFO = "redis_user_info";
    public static final String REDIS_ADMIN_TOKEN = "redis_admin_token";

    public Boolean verifyUserIdToken(String id, String token, String redisKeyPrefix) {
        if (StringUtils.isNoneBlank(id) && StringUtils.isNoneBlank(token)) {
            String userToken = redis.get(redisKeyPrefix + ":" + id);
            if (StringUtils.isBlank(userToken)) {
                GraceException.display(ResponseStatusEnum.NO_AUTH);
                return false;
            } else if (!userToken.equals(token)) {
                GraceException.display(ResponseStatusEnum.NO_AUTH);
                return false;
            }
        } else {
            GraceException.display(ResponseStatusEnum.NO_AUTH);
            return false;
        }
        return true;
    }
}
