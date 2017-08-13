package com.ifeng.fhhService.chen.utils;

import com.ifeng.chen.utils.JackSonUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.FreeMarkerTemplateEngine;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 服务器端辅助工具
 * Created by Chen Weijie on 2017/5/16.
 */
public class ServerUtil {


    private static final Logger LOGGER = LogManager.getLogger(ServerUtil.class);


    /**
     * get请求获取参数
     * @param multiMap
     * @return
     */
    public static Map<String, String> getParams(MultiMap multiMap) {
        if (null == multiMap || multiMap.isEmpty()) {
            return new HashMap<>();
        }

        Map<String, String> params = new HashMap<>();
        List<Map.Entry<String, String>> entryList = multiMap.entries();
        for (Map.Entry<String, String> entry : entryList) {
            String key = entry.getKey();
            String value = entry.getValue();
            params.put(key, value);
        }
        return params;
    }


    /**
     * 渲染模板
     * @param routingContext
     * @param templatePath
     */
    public static void render(RoutingContext routingContext, String templatePath) {

        FreeMarkerTemplateEngine engine = FreeMarkerTemplateEngine.create();
        engine.render(routingContext, templatePath, res -> {
            if (res.succeeded()) {
                try {
                    Buffer buffer = res.result();
                    String result = buffer.toString().replaceAll("[\\r\\n\\t\\\b]", "");
                    if (result.startsWith("[")) {
                        List<Object> list = JackSonUtil.json2List(result, Object.class);
                        result = JackSonUtil.bean2Json(list);
                    } else {
                        Map<String, Object> map = JackSonUtil.json2Map(result, String.class, Object.class);
                        result = JackSonUtil.bean2Json(map);
                    }
                    routingContext.response().end(result);
                } catch (Exception e) {
                    LOGGER.error("render error", e);
                    renderError(routingContext, "server render error");
                }
            } else {
                LOGGER.error("render error", res.cause());
                renderError(routingContext, "server render error");
            }
        });
    }


    /**
     * 渲染失败
     * @param routingContext
     * @param errorMsg
     */
    private static void renderError(RoutingContext routingContext, String errorMsg) {

        JsonObject jsonObject = new JsonObject()
                .put("success", false)
                .put("errorMsg", errorMsg);

        routingContext.response().end(jsonObject.toString());
    }





}
