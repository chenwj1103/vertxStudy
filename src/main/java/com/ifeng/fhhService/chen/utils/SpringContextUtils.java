package com.ifeng.fhhService.chen.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.util.Objects;

/**
 * spring applicationContext utils
 * Created by Chen Weijie on 2017/8/13.
 */
public class SpringContextUtils {

    private static ApplicationContext context;

    private SpringContextUtils() {
    }

    public static void setContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }

    public static Environment getEnv() {
        Objects.requireNonNull(context, "spring applicationContext is null");
        return context.getEnvironment();
    }

}
