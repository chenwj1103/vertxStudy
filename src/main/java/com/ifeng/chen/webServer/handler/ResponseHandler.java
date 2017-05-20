package com.ifeng.chen.webServer.handler;

import com.ifeng.chen.bean.DO.FailedResult;
import com.ifeng.chen.bean.DO.SuccessResult;
import com.ifeng.chen.bean.constant.StatusCodeEnum;

/**
 * Created by Chen Weijie on 2017/5/18.
 */
public abstract class ResponseHandler {


    /**
     * 成功响应方法对象
     * @param data
     * @param statusCodeEnum
     * @return
     */
    public static SuccessResult successResult(Object data, StatusCodeEnum statusCodeEnum) {

        SuccessResult successResult = new SuccessResult();
        successResult.setSuccess(true);
        successResult.setCode(statusCodeEnum.getCode());
        successResult.setData(data);
        return successResult;
    }


    /**
     * 失败响应方法对象
     * @param data
     * @param statusCodeEnum
     * @param msg
     * @return
     */
    public static FailedResult errorResult(Object data, StatusCodeEnum statusCodeEnum, String msg) {

        FailedResult failedResult = new FailedResult();
        failedResult.setSuccess(false);
        failedResult.setData(data);
        failedResult.setCode(statusCodeEnum.getCode());
        failedResult.setMsg(msg);
        return failedResult;
    }

}
