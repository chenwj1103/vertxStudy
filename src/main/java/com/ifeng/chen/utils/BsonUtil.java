package com.ifeng.chen.utils;

import com.ifeng.chen.annotations.BsonIgnore;
import com.ifeng.chen.annotations.BsonProperty;
import com.ifeng.chen.annotations.BsonType;
import com.ifeng.chen.annotations.BsonTypeEnum;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.bson.types.ObjectId;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Created by Chen Weijie on 2017/5/20.
 */
public class BsonUtil {


    /**
     * bean 2 Bson
     *
     * @param t
     * @param escapeNull
     * @param <T>
     * @return
     */
    public static <T> JsonObject bean2Bson(T t, boolean escapeNull) {

        Objects.requireNonNull(t, "object cannot be null");
        Class<?> clazz = t.getClass();
        JsonObject bson = new JsonObject();
        for (Field field : fields(clazz)) {
            try {
                field.setAccessible(true);
                if (checkIgnore(field)) {
                    continue;
                }

                Class<?> fieldType = checkFieldType(field);
                String name = field.getName();
                Object value = field.get(t);
                if (escapeNull && Objects.isNull(value)) {
                    continue;
                }
                value = coverValue(value, clazz);
                bson.put(name, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bson;
    }


    public static <T> JsonObject bean2Bson(T t) {
        return bean2Bson(t, true);
    }


    public static <T> List<JsonObject> list2Bson(boolean escapeNull, List<T> tList) {
        List<JsonObject> list = new ArrayList<>();
        for (T t : tList) {
            JsonObject jsonObject = bean2Bson(t, escapeNull);
            list.add(jsonObject);
        }
        return list;
    }


    public static <T> T bson2Bean(JsonObject bson, Class<T> clazz) throws Exception {

        Objects.requireNonNull(bson, "document cannot be null");
        Objects.requireNonNull(clazz, "class object cannot be null");

        Object object = clazz.newInstance();

        for (Field field : fields(clazz)) {
            field.setAccessible(true);
            if (checkIgnore(field)) {
                continue;
            }

            Class<?> fieldType = checkFieldType(field);
            String name = fieldName(field);
            Object value = bson.getValue(name);
            value = wrapValue(value, fieldType);

            field.set(object, value);
        }

        object = Objects.requireNonNull(object);

        return clazz.cast(object);
    }

    /**
     * 获取bean对象的字段
     *
     * @param clazz
     * @return
     */
    private static Field[] fields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "class object required not null");
        return clazz.getDeclaredFields();
    }


    /**
     * 是否可以忽略
     *
     * @param field
     * @return
     */
    private static boolean checkIgnore(Field field) {
        Objects.requireNonNull(field, "field require not null");
        BsonIgnore ignore = field.getAnnotation(BsonIgnore.class);
        return Objects.nonNull(ignore);

    }

    /**
     * 获取字段名字
     *
     * @param field
     * @return
     */
    private static String fieldName(Field field) {
        String name = field.getName();
        BsonProperty bsonProperty = field.getAnnotation(BsonProperty.class);
        if (Objects.nonNull(bsonProperty)) {
            name = bsonProperty.value();
        }
        return name;
    }


    /**
     * 获取字段的类型
     *
     * @param field
     * @return
     */
    private static Class<?> checkFieldType(Field field) {

        Class<?> clazz = field.getClass();
        BsonType bsonType = field.getAnnotation(BsonType.class);
        if (Objects.nonNull(bsonType)) {
            BsonTypeEnum bsonTypeEnum = bsonType.value();
            if (Objects.equals(bsonType, BsonTypeEnum.BSON_TYPE_ENUM)) {
                clazz = ObjectId.class;
            }
        }
        return clazz;
    }

    /**
     * @param value
     * @param clazz
     * @return
     */
    private static Object coverValue(Object value, Class<?> clazz) {
        if (Objects.nonNull(value) && Objects.nonNull(clazz)) {
            if (Objects.equals(clazz, ObjectId.class)) {
                value = new JsonObject().put("$oid", value);
            } else if (Objects.equals(clazz, Date.class)) {
                Date date = Date.class.cast(value);
                String utcTime = DateUtil.format(date, UTCMatchFormat.UTC_NO_MS1.getFormat(), DateUtil.DEFAULT_TIMEZONE);
                value = new JsonObject().put("$date", utcTime);
            }
        }
        return value;
    }


    private static Object wrapValue(Object value, Class<?> fieldType) {
        if (Objects.nonNull(value)) {
            if (Objects.equals(fieldType, ObjectId.class)) {
                if (value instanceof JsonArray) {
                    JsonArray values = JsonArray.class.cast(value);
                    List<String> list = new ArrayList<>();
                    for (Object object : values) {
                        JsonObject json = JsonObject.class.cast(object);
                        list.add(String.class.cast(json.getString("$oid")));
                    }
                    value = list;
                } else {
                    JsonObject json = JsonObject.class.cast(value);
                    value = json.getString("$oid");
                }
            } else if (Objects.equals(fieldType, Date.class)) {
                JsonObject json = JsonObject.class.cast(value);
                String date = json.getString("$date");
                value = DateUtil.parse(date, DateUtil.utcMatchFormat(date));
            } else if (Objects.equals(fieldType, List.class)) {
                JsonArray jsonArray = JsonArray.class.cast(value);
                value = jsonArray.getList();
            }
        }
        return value;
    }


}
