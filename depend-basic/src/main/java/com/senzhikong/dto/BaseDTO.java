package com.senzhikong.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@ApiModel("基础模型")
public class BaseDTO implements Serializable {
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
