package com.senzhikong.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeConfig;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author Shu.zhou
 * @date 2019年2月25日下午4:45:24
 */
@Slf4j
public class EnumUtil {

    public static <T> T check(Class<T> clz, String code) {
        T unknown = null;
        if (code == null) {
            return unknown;
        }
        try {
            Method codeMethod = clz.getMethod("code");
            for (T obj : clz.getEnumConstants()) {
                String eCode = codeMethod.invoke(obj)
                        .toString();
                if (eCode.equals(code)) {
                    return obj;
                } else if ("unknown".equals(eCode)) {
                    unknown = obj;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return unknown;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> JSONArray toJsonArray(Class<T> clz) {
        Class<Enum> enumClz = (Class<Enum>) clz;
        JSONArray array = new JSONArray();
        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(enumClz);

        for (T obj : clz.getEnumConstants()) {
            String conf = JSON.toJSONString(obj, config);
            array.add(JSONObject.parseObject(conf));
        }
        return array;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> JSONObject toJson(T enumValue) {
        Class<Enum> enumClz = (Class<Enum>) enumValue.getClass();
        SerializeConfig config = new SerializeConfig();
        config.configEnumAsJavaBean(enumClz);
        String conf = JSON.toJSONString(enumValue, config);
        return JSONObject.parseObject(conf);
    }
}
