package com.senzhikong.cache;

import com.alibaba.fastjson.JSON;
import com.senzhikong.util.string.sign.Md5Util;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author Shu.zhou
 * @desc 自定义key生成策略
 */
@Slf4j
@Component
public class SimpleKeyGenerator implements KeyGenerator {

    public static final String NO_PARAM_KEY = "0";
    public static final String NAME = "simpleKeyGenerator";


    @Override
    @NonNull
    public Object generate(Object target, Method method, Object... params) {
        StringBuilder key = new StringBuilder();
        key.append(target.getClass()
                        .getSimpleName())
                .append(".")
                .append(method.getName())
                .append(".");
        if (params.length == 0) {
            return key.append(NO_PARAM_KEY)
                    .toString();
        }
        key.append(paramKey(params));
        return key.toString();
    }

    public static String paramKey(Object... params) {
        if (params.length == 0) {
            return NO_PARAM_KEY;
        }
        String paramStr = JSON.toJSONString(params);
        return Md5Util.getInstance().encode(paramStr);
    }
}
