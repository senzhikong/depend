package com.senzhikong.basic.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * json返回实体类
 *
 * @author Shu.zhou
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseApiResp extends ApiResp<Object> {

    public JSONObject getJsonData() {
        if (getData() == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(getData()));
    }

    public <T> T getData(Class<T> clazz) {
        if (getData() == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(getData()), clazz);
    }
}
