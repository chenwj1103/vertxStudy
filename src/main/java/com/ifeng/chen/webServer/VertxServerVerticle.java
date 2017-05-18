package com.ifeng.chen.webServer;

import com.ifeng.chen.utils.EnvConfigUtils;

import com.ifeng.chen.webServer.handler.*;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.TimeoutHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Chen Weijie on 2017/5/16.
 */
public class VertxServerVerticle extends AbstractVerticle {

    public static final Logger LOGGER = LogManager.getLogger(VertxServerVerticle.class);


    @Override
    public void start(Future<Void> startFuture) throws Exception {


        Integer port = 8081;//Integer.parseInt(EnvConfigUtils.getString("port"));
        Router router = Router.router(vertx);
        route(router);
        vertx.createHttpServer().requestHandler(router::accept).listen(port);
        LOGGER.info("webServer server started! port : " + port);
    }


    private void route(Router router) {
        //基础路由
        basicRoute(router);

        //api路由
        apiRoute(router);

        //测试路由
        testRoute(router);

    }



    private void basicRoute(Router router) {

        //心跳
        router.route("/heartbeat").handler(ctx-> ctx.response().setStatusCode(200).end());

        //5秒超时
        router.route().handler(TimeoutHandler.create(5000));

        //使用cookie
        router.route().handler(CookieHandler.create());

        //使用请求体（post请求使用）
        router.route().handler(BodyHandler.create());

        //通用路由
        router.route().handler(BaseHandler::handAll);
    }


    private void apiRoute(Router router){

        // api设置请求头
        router.route("/sapi/*").handler(BaseHandler::handApi);

        contentRoute(router);
    }













    private void contentRoute(Router router){






    }




    //测试路由
    private void testRoute(Router router) {
        router.route("/hello").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            response.end("hello world from Vertx-Web");
        });
    }



}
