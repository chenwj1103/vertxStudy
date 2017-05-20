package com.ifeng.chen.annotations;

/**
 * Created by Chen Weijie on 2017/5/21.
 */
public enum BsonTypeEnum {


    BSON_TYPE_ENUM("ObjectId");

    BsonTypeEnum(String name) {
        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
