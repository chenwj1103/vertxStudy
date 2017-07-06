package com.ifeng.chen.webServer;

import io.vertx.core.AbstractVerticle;

/**
 * Created by Chen Weijie on 2017/5/25.
 */
public class TimeVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.setTimer(10000,s-> System.out.println(10));
        vertx.setPeriodic(1000, s -> System.out.println(1));
    }

}
