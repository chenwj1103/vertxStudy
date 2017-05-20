package com.ifeng.chen.dao;

import com.ifeng.chen.bean.entity.UserEntity;
import io.vertx.core.Future;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public interface UserDao {


    /**
     * 插入一条用户信息
     * @param userEntity
     * @return
     */
    Future<String> insert(UserEntity userEntity);


    /**
     * 根据主键查找用户信息
     * @param id
     * @return
     */
    Future<UserEntity> findOne(String id);
}
