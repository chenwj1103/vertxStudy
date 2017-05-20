package com.ifeng.chen.bean.DO;

/**
 * Created by Chen Weijie on 2017/5/18.
 */
public class FailedResult {


    //是否返回成功
    private Boolean success = false;

    //返回码
    private Integer code;

    //响应数据
    private Object data;

    //返回错误信息
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
