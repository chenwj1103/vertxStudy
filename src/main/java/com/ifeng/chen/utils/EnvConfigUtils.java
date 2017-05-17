package com.ifeng.chen.utils;

/**
 * Created by Chen Weijie on 2017/5/14.
 */
public class EnvConfigUtils {


    private static String fileName= "config/env_config.json";


    /**
     * 获取配置文件中的配置
     * @param key
     * @return
     */
    public static  String getString(String key){
        return ConfigUtil.getString(fileName,key);
    }


    public static Integer getInteger(String key) {
        return ConfigUtil.getInteger(fileName, key);
    }


}
