package com.ifeng.fhhService.chen.boot;

import com.ifeng.fhhService.chen.SpringVerticleFactory;
import com.ifeng.fhhService.chen.utils.SpringContextUtils;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.spi.VerticleFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

import java.util.Map;
import java.util.Objects;

/**
 * spring容器启动事件监听
 * Created by Chen Weijie on 2017/8/13.
 */
public class BootListener implements ApplicationListener {


    private static final Logger LOGGER = LogManager.getLogger(BootListener.class);


    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if (event instanceof ApplicationStartingEvent){
            LOGGER.info("spring application start");
        }else if (event instanceof ApplicationEnvironmentPreparedEvent){
            LOGGER.info("spring application environmentPrepared");
        }else if (event instanceof ApplicationPreparedEvent){
            LOGGER.info("spring application preparedEvent");
        }else if (event instanceof ApplicationReadyEvent){
            ready(event);
        }else if (event instanceof ApplicationFailedEvent){
            LOGGER.info("spring application start failed");
        }
    }


    private void ready(ApplicationEvent event) {
        LOGGER.info("spring application ready");
        ApplicationReadyEvent applicationReadyEvent = ApplicationReadyEvent.class.cast(event);

        ApplicationContext context = applicationReadyEvent.getApplicationContext();
        SpringContextUtils.setContext(context);

        LOGGER.info("spring context ready, vertx start deploy verticles...");
        VerticleFactory factory = context.getBean("springVerticleFactory", SpringVerticleFactory.class);
        Objects.requireNonNull(factory, "vertx spring verticle factory cannot be null");
        Vertx vertx = context.getBean("vertx", Vertx.class);
        LOGGER.info("vertx...");
        vertx.registerVerticleFactory(factory);
        deploy(vertx, factory, context);

    }


    private void deploy(Vertx vertx, VerticleFactory factory, ApplicationContext context) {

        LOGGER.info("deploy...");
        Map<String, Verticle> verticleMap = context.getBeansOfType(Verticle.class);
        DeploymentOptions options = new DeploymentOptions()
                .setInstances(4);

        if (Objects.nonNull(verticleMap) && !verticleMap.isEmpty()) {
            for (String verticleName : verticleMap.keySet()) {

                String clazzName = verticleMap.get(verticleName).getClass().getName();
                LOGGER.info("vertx deploy verticle {}", clazzName);
                clazzName = factory.prefix() + ":" + clazzName;

                if (options != null) {
                    vertx.deployVerticle(clazzName, options);
                } else {
                    vertx.deployVerticle(clazzName);
                }
            }

        }
    }

}
