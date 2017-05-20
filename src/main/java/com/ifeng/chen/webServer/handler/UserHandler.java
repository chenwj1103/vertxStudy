package com.ifeng.chen.webServer.handler;

import com.ifeng.chen.bean.constant.StatusCodeEnum;
import com.ifeng.chen.bean.entity.UserEntity;
import com.ifeng.chen.service.UserService;
import com.ifeng.chen.service.base.ServiceFactory;
import com.ifeng.chen.utils.ServerUtil;
import com.ifeng.chen.webServer.handler.base.ResponseHandler;
import io.vertx.ext.web.RoutingContext;

import java.util.Map;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public class UserHandler extends ResponseHandler {


    /**
     * 查找用户信息
     * @param routingContext
     */
    public static void findUser(RoutingContext routingContext) {
        Map<String, String> params = routingContext.get("params");
        String id = params.get("id");
        UserService userService = ServiceFactory.getService("userSerice", UserService.class);
        userService.find(id).setHandler(result -> {
            if (result.succeeded()) {
                UserEntity userEntity = result.result();
                routingContext.put("user", successResult(userEntity, StatusCodeEnum.SUCCESS_CODE));
            } else {
                routingContext.put("errorMsg", errorResult(null, StatusCodeEnum.FAILED_CODE, result.cause().getMessage()));
            }
            ServerUtil.render(routingContext, "templates/user.ftl");
        });
    }


}
