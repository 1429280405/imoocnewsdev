package com.imooc.admin.repository;

import com.imooc.pojo.mo.FriendLinkMO;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author liujinqiang
 * @create 2021-08-29 11:45
 */
public interface FriendLinkRepository extends MongoRepository<FriendLinkMO,String> {

}
