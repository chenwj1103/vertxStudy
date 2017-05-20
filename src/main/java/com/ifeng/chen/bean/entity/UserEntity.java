package com.ifeng.chen.bean.entity;

import com.ifeng.chen.annotations.BsonProperty;
import com.ifeng.chen.annotations.BsonType;
import com.ifeng.chen.annotations.BsonTypeEnum;

import java.util.Date;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public class UserEntity {


    //主键
    @BsonProperty("_id")
    @BsonType(BsonTypeEnum.BSON_TYPE_ENUM)
    private String id;

    //用户名
    private String name;

    //用户年龄
    private Integer age;

    //用户住址
    private String address;

    //用户电话
    private String phone;

    //创建时间
    private Date createTime;

    //更新时间
    private Date updateTime;

    //出生日期
    private Date birthday;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
