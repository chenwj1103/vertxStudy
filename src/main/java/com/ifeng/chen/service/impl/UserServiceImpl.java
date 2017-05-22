package com.ifeng.chen.service.impl;

import com.ifeng.chen.annotations.ServiceImpl;
import com.ifeng.chen.bean.entity.UserEntity;
import com.ifeng.chen.dao.UserDao;
import com.ifeng.chen.dao.base.DaoFactory;
import com.ifeng.chen.dao.impl.UserDaoImpl;
import com.ifeng.chen.service.UserService;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
@ServiceImpl("userService")
public class UserServiceImpl implements UserService {


    private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

    @Override
    public Future<UserEntity> find(String id) {
        UserDao userDao = DaoFactory.getDao("userDao", UserDao.class);
        Future<UserEntity> future = Future.future();
        try {
            Objects.requireNonNull(userDao, "userDao require not null");
            Objects.requireNonNull(id, "id require not null");
            userDao.findOne(id).compose(future::complete, future);
        } catch (Exception e) {
            LOGGER.error(e);
            future.fail(e);
        }
        return future;
    }

    @Override
    public Future<String> insert(UserEntity userEntity) {
        UserDao userDao = DaoFactory.getDao("userDao", UserDao.class);
        Future<String> future = Future.future();
        try {
            Objects.requireNonNull(userDao, "userDao require not null");
            Objects.requireNonNull(userEntity, "userEntity require not null");
            userDao.insert(userEntity).compose(future::complete,future);
        } catch (Exception e) {
            LOGGER.error(e);
            future.fail(e);
        }
        return future;
    }
}
