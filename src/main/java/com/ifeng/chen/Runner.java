package com.ifeng.chen;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

import java.util.function.Consumer;


/**
 * Created by Chen Weijie on 2017/5/15.
 */
public class Runner {


    public static void run(String verticleId, VertxOptions vertxOptions, DeploymentOptions deploymentOptions) {

        if (vertxOptions == null) {
            vertxOptions = new VertxOptions();
        }
        Consumer<Vertx> runner = vertx -> {
            try {
                if (deploymentOptions != null) {
                    vertx.deployVerticle(verticleId, deploymentOptions);
                } else {
                    vertx.deployVerticle(verticleId);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        if (vertxOptions.isClustered()) {
            Vertx.clusteredVertx(vertxOptions, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(vertxOptions);
            runner.accept(vertx);
        }
    }


    public static void run(VertxOptions options, DeploymentOptions deploymentOptions, String... verticleIDs) {
        if (options == null) {
            // Default parameter
            options = new VertxOptions();
        }

        Consumer<Vertx> runner = vertx -> {
            try {
                for(String verticleID : verticleIDs){
                    if (deploymentOptions != null) {
                        vertx.deployVerticle(verticleID, deploymentOptions);
                    } else {
                        vertx.deployVerticle(verticleID);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        };
        if (options.isClustered()) {
            Vertx.clusteredVertx(options, res -> {
                if (res.succeeded()) {
                    Vertx vertx = res.result();
                    runner.accept(vertx);
                } else {
                    res.cause().printStackTrace();
                }
            });
        } else {
            Vertx vertx = Vertx.vertx(options);
            runner.accept(vertx);
        }
    }

    public static void run(Class clazz) {
        run(clazz, new VertxOptions().setClustered(false), null);
    }

    public static void run(Class clazz, DeploymentOptions options) {
        run(clazz, new VertxOptions().setClustered(false), options);
    }

    public static void run(Class clazz, VertxOptions options, DeploymentOptions deploymentOptions) {
        run(clazz.getName(), options, deploymentOptions);
    }


}
