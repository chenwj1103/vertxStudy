package com.ifeng.chen.service.impl;

import com.ifeng.chen.annotations.ServiceImpl;
import com.ifeng.chen.bean.entity.UserEntity;
import com.ifeng.chen.dao.UserDao;
import com.ifeng.chen.dao.base.DaoFactory;
import com.ifeng.chen.service.UserService;
import io.vertx.core.Future;

import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
@ServiceImpl("userService")
public class UserServiceImpl implements UserService {

    @Override
    public Future<UserEntity> find(String id) {
        UserDao userDao = DaoFactory.getDao("userDao", UserDao.class);
        Future<UserEntity> future = Future.future();
        try {
            Objects.requireNonNull(userDao, "userDao require not null");
            Objects.requireNonNull(id, "id require not null");
            userDao.findOne(id).compose(future::complete, future);
        } catch (Exception e) {
            future.fail(e);
        }
        return future;
    }
}
