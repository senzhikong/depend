package com.senzhikong.basic.vo;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@Data
@Accessors(chain = true)
public class BaseEntityVO implements Serializable {
    protected Long id;
    protected Integer status;
    protected Date createTime;
    protected Long createBy;
    protected Date updateTime;
    protected Long updateBy;
    protected String remark;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
