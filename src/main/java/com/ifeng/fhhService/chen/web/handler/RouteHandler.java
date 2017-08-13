package com.ifeng.fhhService.chen.web.handler;

import com.ifeng.chen.utils.JackSonUtil;
import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 路由处理接口
 * Created by Chen Weijie on 2017/8/13.
 */
public interface RouteHandler {


    String CONTENT_TYPE_FORM_DATA = "multipart/form-data";

    String CONTENT_TYPE_FORM_URLENCODED = "application/x-www-form-urlencoded";

    /**
     * 路由地址
     *
     * @return url
     */
    String route();


    /**
     * 请求处理
     *
     * @param routingContext
     */
    void handle(RoutingContext routingContext);


    default String postBody(RoutingContext routingContext) {
        return routingContext.getBodyAsString();
    }


    default Map<String, String> httpParams(RoutingContext routingContext) {

        HttpMethod method = routingContext.request().method();
        Map<String, String> params = routingContext.get("_parameterMap");
        if (params == null || params.isEmpty()) {
            if (Objects.equals(HttpMethod.GET, method)) {
                params = httpGetParams(routingContext);
            } else if (Objects.equals(HttpMethod.POST, method)) {
                params = httpPostParams(routingContext);
            }
            routingContext.put("_parameterMap", params);
        }
        return params;
    }


    default Map<String, String> httpGetParams(RoutingContext routingContext) {

        MultiMap paramMap = routingContext.request().params();
        return Stream.of(paramMap)
                .flatMap(pramMap -> paramMap.entries().stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }


    default Map<String, String> httpPostParams(RoutingContext routingContext) {

        Map<String, String> params;
        String contentType = routingContext.request().getHeader("Content_Type");
        if (Objects.nonNull(contentType) && (contentType.contains(CONTENT_TYPE_FORM_DATA)
                || contentType.contains(CONTENT_TYPE_FORM_URLENCODED))) {
            params = Stream.of(routingContext.request().params())
                    .flatMap(multiMap -> multiMap.entries().stream())
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            params = parseBodyParams(routingContext.getBodyAsString());
        }
        return params;
    }


    default Map<String, String> parseBodyParams(String body) {
        Map<String, String> params = new HashMap<>();
        if (Objects.nonNull(body)) {
            Arrays.stream(body.split("&"))
                    .map(paramsEntry -> paramsEntry.split("="))
                    .filter(strings -> strings.length == 2)
                    .filter(strings -> Objects.nonNull(strings[0]) && !"".equals(strings[0]))
                    .forEach(strings -> params.put(strings[0], strings[1]));
        }
        return params;
    }


    default void response(String res, RoutingContext routingContext) {

        HttpServerResponse response = routingContext.response();
        if (!response.ended()) {
            response.end(res);
        }

        if (!response.closed()) {
            response.close();
        }
    }

    default String getPath(RoutingContext routingContext) {
        return routingContext.request().path();

    }


    /**
     * 失败响应
     *
     * @param errorMessage 错误信息
     * @return 失败响应结果
     */
    default String failResponse(String errorMessage) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", errorMessage);
        map.put("data", null);
        return JackSonUtil.bean2Json(map);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return 成功响应结果
     * @throws Exception
     */
    default String succeedResponse(Object data) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("code", 1);
        map.put("msg", "success");
        map.put("data", data);

        return JackSonUtil.bean2Json(map);
    }

    /**
     * 异常响应(最终处理)
     *
     * @param e 异常
     * @return
     */
    default String exceptionResponse(Throwable e) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("code", 0);
            map.put("msg", e.getMessage());
            map.put("data", null);
            return JackSonUtil.bean2Json(map);
        } catch (Exception e1) {
            return "{\"code\":0,\"data\":null,\"msg\":\"system error\"}";
        }
    }

}
