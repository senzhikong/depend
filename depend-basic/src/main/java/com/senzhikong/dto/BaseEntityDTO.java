package com.senzhikong.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@Data
@ApiModel("基础对象模型")
public class BaseEntityDTO implements Serializable {
    @Schema(description = "ID")
    protected Long id;
    @Schema(description = "状态")
    protected String status;
    @Schema(description = "状态")
    protected String statusDesc;
    @Schema(description = "创建时间")
    protected Date createTime;
    @Schema(description = "创建人")
    protected Long createBy;
    @Schema(description = "更新时间")
    protected Date updateTime;
    @Schema(description = "更新人")
    protected Long updateBy;
    @Schema(description = "备注")
    protected String remark;

    public String toString() {
        return JSON.toJSONString(this);
    }
}
