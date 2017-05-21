package com.ifeng.chen.dao.base;

import com.ifeng.chen.annotations.DaoImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 数据访问工厂实现
 *
 * Created by Chen Weijie on 2017/5/18.
 */
public class DaoFactory {


    private  static final Logger LOGGER= LogManager.getLogger(DaoFactory.class);

    private static final Map<String,Object> DAOS_MAP=new HashMap<>();

    private DaoFactory(){}


    public static <T> T getDao(String name, Class<T> clazz) {
        Object object = DAOS_MAP.get(name);
        try {
            Objects.requireNonNull(name, "name cannot be empty");
            if (Objects.isNull(object)) {
                Class<?> aClass = findClass(name);
                if (Objects.nonNull(aClass)) {
                    object = aClass.newInstance();
                    DAOS_MAP.put(name, object);
                    LOGGER.info("create dao pojo success : {}", clazz.getName());
                }
            }
        } catch (Exception e) {
            LOGGER.error(e);
        }
        return clazz.cast(object);
    }


    private static Class<?> findClass(String name) {
        Reflections reflections = new Reflections("com.ifeng.chen.dao.impl");
        Set<Class<?>> classSet = reflections.getTypesAnnotatedWith(DaoImpl.class);
        for (Class<?> clazz : classSet) {
            DaoImpl daoImpl = clazz.getAnnotation(DaoImpl.class);
            if (Objects.nonNull(daoImpl)) {
                String key = daoImpl.value();
                if (Objects.equals(key, name)) {
                    return clazz;
                }
//                break;
            }
        }
        return null;
    }





}
