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
 * Created by Chen Weijie on 2017/5/23.
 */
public class BsonUtils {


    public static <T> List<JsonObject> list2Bson(List<T> list, boolean escapeNull) {
        List<JsonObject> documents = new ArrayList<>();
        for (T t : list) {
            documents.add(bean2Bson(t, escapeNull));
        }
        return documents;
    }

    public static <T> JsonObject bean2Bson(T t) {
        return bean2Bson(t, true);
    }

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
                Class<?> fieldType = checkFieldTypeWithBson(field);
                String name = fieldName(field);
                Object value = field.get(t);
                if (escapeNull && Objects.isNull(value)) {
                    continue;
                }
                value = convertValue(value, fieldType);
                bson.put(name, value);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return bson;
    }

    public static <T> List<T> bson2List(List<JsonObject> bsons, Class<T> clazz) throws Exception {
        List<T> list = new ArrayList<>();
        for (JsonObject bson : bsons) {
            list.add(bson2Bean(bson, clazz));
        }
        return list;
    }

    public static <T> T bson2Bean(JsonObject bson, Class<T> clazz) throws Exception {
        Objects.requireNonNull(bson, "document cannot be null");
        Objects.requireNonNull(clazz, "class object cannot be null");
        Object o = clazz.newInstance();
        for (Field field : fields(clazz)) {
            field.setAccessible(true);
            if (checkIgnore(field))
                continue;
            /*字段类型*/
            Class<?> fieldType = checkFieldTypeWithBson(field);
            String name = fieldName(field);
            Object value = bson.getValue(name);
            value = wrapValue(value, fieldType);
            field.set(o, value);
        }

        o = Objects.requireNonNull(o);
        return clazz.cast(o);
    }

    private static Object convertValue(Object value, Class<?> clazz) {
        if (Objects.nonNull(value) && Objects.nonNull(clazz)) {
            if (Objects.equals(clazz, ObjectId.class)) {
                value = new JsonObject().put("$oid", value);
            } else if (Objects.equals(clazz, Date.class)) {
                Date date = Date.class.cast(value);
                String utcTime = DateUtil.format(date, UTCMatchFormat.UTC_FULL.getFormat());
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

    private static Class<?> checkFieldTypeWithBson(Field field) {
        Class<?> clazz = field.getType();

        /*bsonTyoe注解*/
        BsonType bsonType = field.getAnnotation(BsonType.class);
        if (Objects.nonNull(bsonType)) {
            BsonTypeEnum type = bsonType.value();
            if (Objects.equals(type, BsonTypeEnum.BSON_TYPE_ENUM)) {
                clazz = ObjectId.class;
            }
        }
        return clazz;
    }

    private static String fieldName(Field field) {
        String name = field.getName();
        BsonProperty property = field.getAnnotation(BsonProperty.class);
        if (Objects.nonNull(property)) {
            name = property.value();
        }
        return name;
    }

    private static boolean checkIgnore(Field field) {
        BsonIgnore ignore = field.getAnnotation(BsonIgnore.class);
        return Objects.nonNull(ignore);
    }

    private static Field[] fields(Class<?> clazz) {
        Objects.requireNonNull(clazz, "class object cannot be null");
        return clazz.getDeclaredFields();
    }

}
