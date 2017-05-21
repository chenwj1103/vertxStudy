package com.ifeng.chen.service;

import com.ifeng.chen.bean.entity.UserEntity;
import io.vertx.core.Future;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public interface UserService {


    /**
     * 查询用户信息
     * @param id
     * @return
     */
    Future<UserEntity> find(String id);


    /**
     * 插入用户信息
     *
     * @param userEntity
     * @return
     */
    Future<String> insert(UserEntity userEntity);

}
