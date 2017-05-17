package com.ifeng.chen.utils;

import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Chen Weijie on 2017/5/14.
 */
public class ConfigUtil {


    private static final Logger LOGGER= LogManager.getLogger(ConfigUtil.class);

    private static HashMap<String,JsonObject> configMap=new HashMap<String, JsonObject>();


    //加载默认配置
    static {
        LOGGER.info("loading config file...");
        initConfigFile("config/env_config.json");
        LOGGER.info("load finish");
    }


    /**
     * 获取配置文件中的配置
     * @param fileName
     * @param key
     * @return
     */
    public static String getString(String fileName,String key){
        JsonObject config=configMap.get(fileName);
        return config.getString(key);
    }

    /**
     * 获取配置文件中的配置
     * @param fileName
     * @param key
     * @return
     */
    public static Integer getInteger(String fileName, String key) {
        JsonObject config = configMap.get(fileName);
        return config.getInteger(key);
    }


    /**
     * 获取配置文件中的配置
     * @param fileName
     * @param key
     * @return
     */
    public static Boolean getBoolean(String fileName,String key){

        JsonObject config=configMap.get(fileName);
        return config.getBoolean(key);
    }





    private static void initConfigFile(String fileName) {
        try {
            URL url = ConfigUtil.class.getClassLoader().getResource(fileName);
            Map<String, Object> map = JackSonUtil.url2Map(url, String.class, Object.class);
            if (null == map || map.isEmpty()) {
                throw new Exception("加载" + fileName + "失败！");
            }
            JsonObject jsonObject = new JsonObject(map);
            configMap.put(fileName, jsonObject);
            LOGGER.info("加载" + fileName + "成功");
        } catch (Exception e) {
            LOGGER.info("加载" + fileName + "失败", e);
        }
    }




}
