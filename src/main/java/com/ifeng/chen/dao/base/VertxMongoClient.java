package com.ifeng.chen.dao.base;

import com.ifeng.chen.utils.EnvConfigUtil;
import com.ifeng.chen.utils.VertxConstant;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/20.
 */
public class VertxMongoClient {


    private static final Logger LOGGER = LogManager.getLogger(VertxMongoClient.class);

    private static final String mongoDBDriver = EnvConfigUtil.getString("mongoDB.driver");

    private static final String mongoDBPool = EnvConfigUtil.getString("mongo.poolname");

    private VertxMongoClient() {
    }

    private static MongoClient vertxMongoClient = null;

    public static MongoClient getVertxMongoClient() {
        if (Objects.isNull(vertxMongoClient)) {
            JsonObject config = new JsonObject()
                    .put("connection_string", mongoDBDriver)
//                    .put("connection_string","mongodb://127.0.0.1:27017/vertxStudy")
                    .put("useObjectId", true);
            System.out.println("config===="+config);
            vertxMongoClient = MongoClient.createShared(VertxConstant.vertxStatic, config, mongoDBPool);
        }

        return vertxMongoClient;
    }
}
