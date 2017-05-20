package com.ifeng.chen.service.base;

import com.ifeng.chen.annotations.ServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by Chen Weijie on 2017/5/20.
 */
public class ServiceFactory {


    private static final Logger LOGGER = LogManager.getLogger(ServiceFactory.class);

    private static final Map<String, Object> SERVICE_MAP = new HashMap<>();


    public static <T> T getService(String name, Class<T> clazz) {

        Object object = SERVICE_MAP.get(name);
        try {
            Objects.requireNonNull(name, "service name require not null");
            if (Objects.isNull(object)) {
                Class<?> clz = findClass(name);
                object = clz.newInstance();
                SERVICE_MAP.put(name, object);
                LOGGER.info("create service pojo success : {}", clz.getName());
            }
        } catch (Exception e) {
            LOGGER.error("create service pojo fail", e);
        }
        return clazz.cast(object);
    }


    private static Class<?> findClass(String name) {

        Reflections REFLECTIONS = new Reflections("com.ifeng.chen.service.impl");
        Set<Class<?>> classSet = REFLECTIONS.getTypesAnnotatedWith(ServiceImpl.class);
        for (Class<?> clazz : classSet) {
            ServiceImpl serviceImpl = clazz.getAnnotation(ServiceImpl.class);
            if (Objects.nonNull(serviceImpl)) {
                String key = serviceImpl.value();
                if (Objects.equals(name, key)) {
                    return clazz;
                }
            }
        }
        return null;
    }


}
