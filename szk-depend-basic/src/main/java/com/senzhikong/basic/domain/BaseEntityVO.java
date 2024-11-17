package com.senzhikong.basic.domain;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@Data
public class BaseEntityVO implements Serializable {
    protected String id;
    protected String status;
    protected Date createTime;
    protected String createBy;
    protected Date updateTime;
    protected String updateBy;
    protected String remark;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
