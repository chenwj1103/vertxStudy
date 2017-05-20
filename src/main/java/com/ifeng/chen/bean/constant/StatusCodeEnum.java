package com.ifeng.chen.bean.constant;

/**
 * Created by Chen Weijie on 2017/5/18.
 */
public enum StatusCodeEnum {

    SUCCESS_CODE("成功", 1),
    FAILED_CODE("失败", 0),
    TIMEOUT_CODE("响应超时", 2);

    private String name;

    private Integer code;

    StatusCodeEnum(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
