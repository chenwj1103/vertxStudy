package com.ifeng.chen.dao.impl;

import com.ifeng.chen.annotations.DaoImpl;
import com.ifeng.chen.bean.entity.UserEntity;
import com.ifeng.chen.dao.UserDao;
import com.ifeng.chen.dao.base.AbstractVertxMongoDB;
import com.ifeng.chen.dao.constant.CollectionName;
import com.ifeng.chen.utils.BsonUtil;
import com.ifeng.chen.utils.BsonUtils;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
@DaoImpl("userDao")
public class UserDaoImpl extends AbstractVertxMongoDB implements UserDao {


    private static final Logger LOGGER = LogManager.getLogger(UserDaoImpl.class);

    @Override
    public Future<UserEntity> findOne(String id) {
        Future<UserEntity> future = Future.future();

        try {
            Objects.requireNonNull(id, "required id not empty");

            validateObjectId(id);
            JsonObject query = new JsonObject()
                    .put("_id", new JsonObject()
                            .put("$oid", id));
            findOne(query).compose(document -> {
                UserEntity userEntity = null;
                try {
                    if (Objects.nonNull(document) && !document.isEmpty()) {
                        System.out.println("document==="+document);
                        userEntity = BsonUtil.bson2Bean(document, UserEntity.class);
                    }
                    future.complete(userEntity);
                } catch (Exception e) {
                    LOGGER.error("bson 2 userEntity failed", e);
                    future.fail(e);
                }
            }, future);
        } catch (Exception e) {
            LOGGER.error("find userEntity failed", e);
            future.fail(e);
        }
        return future;
    }

    @Override
    public Future<String> insert(UserEntity userEntity) {

        Future<String> future = Future.future();
        try {
            Objects.requireNonNull(userEntity, "required userEntity not empty");
            JsonObject bson = BsonUtil.bean2Bson(userEntity);
            insertOne(bson).compose(future::complete, future);
        } catch (Exception e) {
            LOGGER.error(e);
            future.fail(e);
        }
        return future;
    }

    @Override
    public String getCollection() {
        return CollectionName.USER;
    }
}
