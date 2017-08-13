package com.ifeng.fhhService.chen;

import io.vertx.core.Verticle;
import io.vertx.core.spi.VerticleFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring verticle 工厂
 * Created by Chen Weijie on 2017/8/13.
 */
public class SpringVerticleFactory implements VerticleFactory ,ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Override
    public boolean blockingCreate() {
        return true;
    }

    @Override
    public String prefix() {
        return "springContext";
    }

    @Override
    public Verticle createVerticle(String verticleName, ClassLoader classLoader) throws Exception {
        String clazz = VerticleFactory.removePrefix(verticleName);
        return (Verticle) applicationContext.getBean(Class.forName(clazz));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
