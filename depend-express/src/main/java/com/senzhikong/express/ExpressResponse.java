package com.senzhikong.express;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import lombok.Data;

enum Status {

    // (status：collected-收件；sending-运送中；delivering-派件中；signed-签收；question-问题件)
    UNKNOW(0, "无信息"),
    COLLECTED(1, "已揽件"),
    SENDING(2, "运送中"),
    DELIVERING(3, "派件中"),
    SIGNED(4, "签收"),
    QUESTION(5, "问题件");

    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

}

/**
 * @author Shu.zhou
 * @date 2018年12月12日上午10:31:57
 */
@Data
public class ExpressResponse {

    private Boolean success = false;
    private Integer code;
    private String msg;
    private String serialNumber;
    private Status status = Status.UNKNOW;
    private JSONArray expressList;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}