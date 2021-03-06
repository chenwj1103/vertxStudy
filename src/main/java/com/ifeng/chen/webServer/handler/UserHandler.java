package com.ifeng.chen.webServer.handler;

import com.ifeng.chen.bean.constant.StatusCodeEnum;
import com.ifeng.chen.bean.entity.UserEntity;
import com.ifeng.chen.service.UserService;
import com.ifeng.chen.service.base.ServiceFactory;
import com.ifeng.chen.utils.DateUtil;
import com.ifeng.chen.utils.JackSonUtil;
import com.ifeng.chen.utils.ServerUtil;
import com.ifeng.chen.webServer.handler.base.ResponseHandler;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public class UserHandler extends ResponseHandler {

    private static final Logger LOGGER= LogManager.getLogger(UserHandler.class);

    /**
     * 查找用户信息
     * @param routingContext
     */
    public static void findUser(RoutingContext routingContext) {
        Map<String, String> params = routingContext.get("params");
        String id = params.get("id");
        UserService userService = ServiceFactory.getService("userService", UserService.class);
        userService.find(id).setHandler(result -> {
            if (result.succeeded()) {
                UserEntity userEntity = result.result();
                LOGGER.info("user:{}",JackSonUtil.bean2Json(userEntity));
                routingContext.put("result", successResult(userEntity, StatusCodeEnum.SUCCESS_CODE));
            } else {
                routingContext.put("errorMsg", errorResult(null, StatusCodeEnum.FAILED_CODE, result.cause().getMessage()));
            }
            ServerUtil.render(routingContext, "templates/user.ftl");
        });
    }


    /**
     * 插入用户信息
     *
     * @param routingContext
     */
    public static void insertUser(RoutingContext routingContext) {
        UserEntity userEntity = new UserEntity();
        userEntity.setAddress("北京市-立水桥");
        userEntity.setAge(25);
        userEntity.setBirthday(new Date());
        userEntity.setCreateTime(new Date());
        userEntity.setName("chenweijie");
        userEntity.setPhone("15321939301");

        UserService userService = ServiceFactory.getService("userService", UserService.class);
        userService.insert(userEntity).setHandler(result -> {
            String response;
            if (result.succeeded()) {
                response = "{\"success\":true}";
            } else {
                response = "{\"success\":false}";
            }
            routingContext.response().end(response);
        });
    }




}
