package com.ifeng.fhhService.chen;

import com.ifeng.chen.utils.EnvConfigUtil;
import com.ifeng.fhhService.chen.web.handler.RouteHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;
import java.util.Objects;

/**
 * spring启动web服务
 * Created by Chen Weijie on 2017/8/13.
 */
public class SpringWebVerticle extends AbstractVerticle implements ApplicationContextAware {


    private static final Logger LOGGER = LogManager.getLogger(SpringWebVerticle.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }


    @Override
    public void start(Future<Void> startFuture) throws Exception {

        try {
            int port = Integer.parseInt(EnvConfigUtil.getString("port"));
            Router router = Router.router(vertx);
            route(router);
            HttpServer server = vertx.createHttpServer();
            server.requestHandler(router::accept).listen(port);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }


    /**
     * 路由
     *
     * @param router
     */
    private void route(Router router) {

        //基础路由
        basicRoutes(router);


        Map<String, RouteHandler> handlerMap = applicationContext.getBeansOfType(RouteHandler.class);

        if (Objects.nonNull(handlerMap) && !handlerMap.isEmpty()) {

            handlerMap.entrySet().forEach(entry -> {

                RouteHandler routeHandler = entry.getValue();
                String handlerName = entry.getKey();

                if (routeHandler != null) {
                    String route = routeHandler.route();
                    router.route(route).handler(routeHandler::handle);
                    LOGGER.info("{} handle route {} ok !", handlerName, route);
                }

            });

        }

    }


    /**
     * 基础路由
     *
     * @param router 路由
     */
    private void basicRoutes(Router router) {
        //post请求体
        router.route().handler(BodyHandler.create());

        //心跳
        router.route("/heartbeat").handler(ctx -> {
            ctx.response().setStatusCode(200).end();
            ctx.response().close();
        });

        router.route().handler(ctx -> {
            ctx.response().putHeader("Content-Type", "application/json;charset=utf-8");
            ctx.next();

        });
    }

}
