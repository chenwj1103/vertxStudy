package com.ifeng.chen;

import com.ifeng.chen.utils.EnvConfigUtil;
import com.ifeng.chen.webServer.VertxServerVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.VertxOptions;

/**
 * Created by Chen Weijie on 2017/5/14.
 */
public class Main {

    public static void main(String args[]){


        VertxOptions options = new VertxOptions();

        DeploymentOptions deploymentOptions;
        String env = EnvConfigUtil.getString("env");
        if ("test".equals(env)) {
            deploymentOptions = new DeploymentOptions();
            int core = Runtime.getRuntime().availableProcessors();
            deploymentOptions.setInstances(core);
        } else if ("prod".equals(env)) {
            deploymentOptions = new DeploymentOptions();
            deploymentOptions.setInstances(4);
        } else {
            deploymentOptions = new DeploymentOptions();
            deploymentOptions.setInstances(2);
        }

        Runner.run(options,deploymentOptions, VertxServerVerticle.class.getName());

    }

}
