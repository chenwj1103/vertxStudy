package com.ifeng.chen.dao.base;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.mongo.UpdateOptions;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/20.
 */
public abstract class AbstractVertxMongoDB {


    private MongoClient mongoClient = VertxMongoClient.getVertxMongoClient();


    /**
     * 查询一条记录
     *
     * @param query 查询条件
     * @return 查询结果
     */
    public Future<JsonObject> findOne(JsonObject query) {

        Future<JsonObject> future = Future.future();
        System.out.println("getCollection()==="+getCollection());
        System.out.println("query()==="+query);
        mongoClient.findOne(getCollection(), query, new JsonObject(), result -> {
            if (result.succeeded()) {
                JsonObject jsonObject=result.result();
                System.out.println("jsonObject()==="+jsonObject);
                future.complete(jsonObject);
            } else {
                future.fail(result.cause());
            }
        });
        return future;
    }


    /**
     * 插入一条记录
     *
     * @param json 要插入的对象
     * @return 插入对象的主键
     */
    public Future<String> insertOne(JsonObject json) {
        Future<String> future = Future.future();
        mongoClient.insert(getCollection(), json, result -> {
            if (result.succeeded()) {
                System.out.println("json============"+json);
                future.complete(result.result());
            } else {
                future.fail(result.cause());
            }
        });
        return future;
    }


    /**
     * 查询文档的数量
     *
     * @param query 查询条件
     * @return 返回数量
     */
    public Future<Long> count(JsonObject query) {
        Future<Long> future = Future.future();
        mongoClient.count(getCollection(), query, result -> {
            if (result.succeeded()) {
                future.complete(result.result());
            } else {
                future.fail(result.cause());
            }
        });
        return future;
    }

    /**
     * 查询文档列表
     *
     * @param query 查询条件
     * @return 文档列表
     */
    public Future<List<JsonObject>> findList(JsonObject query) {

        Future<List<JsonObject>> future = Future.future();
        mongoClient.find(getCollection(), query, result -> {
            if (result.succeeded()) {
                future.complete(result.result());
            } else {
                future.fail(result.cause());
            }
        });

        return future;
    }

    /**
     * 更新对象
     *
     * @param query            查询条件
     * @param updateJsonObject 被更新的对象
     * @return 客户端更新结果
     */
    public Future<MongoClientUpdateResult> updateOne(JsonObject query, JsonObject updateJsonObject) {

        Future<MongoClientUpdateResult> future = Future.future();
        UpdateOptions updateOptions = new UpdateOptions()
                .setMulti(false);
        mongoClient.updateCollectionWithOptions(getCollection(), query, updateJsonObject, updateOptions, result -> {
            if (result.succeeded()) {
                future.complete(result.result());
            } else {
                future.fail(result.cause());
            }
        });
        return future;
    }


    /**
     * 批量插入文档操作
     *
     * @param jsonObjectList 文档列表
     * @return 是否插入成功
     */
    public Future<Boolean> insertMany(List<JsonObject> jsonObjectList) {

        Future<Boolean> future = Future.future();
        JsonArray jsonArray = new JsonArray();
        List<JsonObject> list = new ArrayList<>();
        jsonObjectList.forEach(document -> {
            String _id = new ObjectId().toHexString();
            document.put("_id", new JsonObject()
                    .put("$oid", _id));
            list.add(document);
        });

        JsonObject command = new JsonObject()
                .put("insert", getCollection())
                .put("documents", jsonArray)
                .put("ordered", false);

        mongoClient.runCommand("insert", command, result -> {
            if (result.succeeded()) {
                if (result.result().getInteger("ok") == 1) {
                    future.complete(true);
                } else {
                    future.complete(false);
                }

            } else {
                future.fail(result.cause());
            }
        });
        return future;
    }

    /**
     * 校验ObjectId
     * @param id
     * @throws Exception
     */
    public void validateObjetId(String id) throws Exception {
        Objects.requireNonNull(id, "validateObjetId id require not null");
        new ObjectId(id);
    }

    public abstract String getCollection();

}
