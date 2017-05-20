package com.ifeng.chen.webServer.handler.base;

import com.ifeng.chen.utils.ServerUtil;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Created by Chen Weijie on 2017/5/16.
 */
public class BaseHandler {


    private static final Logger LOGGER= LogManager.getLogger(BaseHandler.class);



    public static void handAll(RoutingContext routingContext){

        String uri = routingContext.request().uri();
        String host = routingContext.request().host();
        LOGGER.info("request uri:{}", uri);
        LOGGER.info("request host:{}", host);

        //设置响应
        HttpServerResponse response=routingContext.response();
        response.setChunked(true);
        rountNext(routingContext);
    }



    private static void rountNext(RoutingContext routingContext){

        try {
            Map<String,String> params= ServerUtil.getParams(routingContext.request().params());
            routingContext.put("params",params);
            routingContext.next();
        } catch (Exception e) {
            LOGGER.error("parse params failed",e);
            routingContext.fail(503);
        }

    }


    public static void handApi(RoutingContext routingContext){

        //设置响应头
        routingContext.response().putHeader("content-type","application/json;charset=UTF-8");
        routingContext.next();
    }






}
