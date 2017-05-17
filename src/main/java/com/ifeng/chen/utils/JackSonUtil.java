package com.ifeng.chen.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chen Weijie on 2017/5/14.
 */
public class JackSonUtil {


    private static final Logger LOGGER = LogManager.getLogger(JackSonUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ObjectMapper mapperWithNull = new ObjectMapper();

    static {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        DateFormat smp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mapper.setDateFormat(smp);
    }

    /**
     * json字符串转bean
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T json2bean(String json, Class<T> clazz) {

        try {
            return mapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("json 2 bean failed", e);
        }
        return null;
    }


    /**
     * bean to json
     * @param object
     * @return
     */
    public static String bean2Json(Object object){

        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            LOGGER.error("bean 2 json failed",e);
        }

        return null;
    }


    /**
     * json to list
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> List<T> json2List(String json, Class<T> clazz) {
        JavaType type = mapper.getTypeFactory().constructCollectionLikeType(ArrayList.class, clazz);

        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error("json 2 list failed", e);
        }
        return null;
    }


    /**
     * json to Map
     *
     * @param json
     * @param keyClass
     * @param valueClass
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> Map<T1, T2> json2Map(String json, Class<T1> keyClass, Class<T2> valueClass) {
        JavaType type = mapper.getTypeFactory().constructMapLikeType(HashMap.class, keyClass, valueClass);
        try {
            return mapper.readValue(json, type);
        } catch (IOException e) {
            LOGGER.error("json 2 map failed", e);
        }
        return null;
    }


    /**
     * url 2 Map
     *
     * @param url
     * @param keyClass
     * @param valueCLass
     * @param <T1>
     * @param <T2>
     * @return
     */
    public static <T1, T2> Map<T1, T2> url2Map(URL url, Class<T1> keyClass, Class<T2> valueCLass) {
        JavaType type = mapper.getTypeFactory().constructMapLikeType(HashMap.class, keyClass, valueCLass);
        try {
            return mapper.readValue(url, type);
        } catch (IOException e) {
            LOGGER.error(" url 2 map failed", e);
        }
        return null;
    }


}
