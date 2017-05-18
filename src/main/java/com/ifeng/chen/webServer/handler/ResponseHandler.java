package com.ifeng.chen.webServer.handler;

import com.ifeng.chen.bean.DO.ErrorResult;
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
    public static ErrorResult errorResult(Object data, StatusCodeEnum statusCodeEnum, String msg) {

        ErrorResult errorResult = new ErrorResult();
        errorResult.setSuccess(false);
        errorResult.setData(data);
        errorResult.setCode(statusCodeEnum.getCode());
        errorResult.setMsg(msg);
        return errorResult;
    }

}
