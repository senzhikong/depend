package com.senzhikong.basic.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shu
 */
@Data
@FieldNameConstants
@Schema(name = "基础对象模型")
public class BaseEntityDTO implements Serializable {
    @Schema(description = "ID")
    protected String id;
    @Schema(description = "状态")
    protected String status;
    @Schema(description = "创建时间")
    protected Date createTime;
    @Schema(description = "创建人")
    protected String createBy;
    @Schema(description = "更新时间")
    protected Date updateTime;
    @Schema(description = "更新人")
    protected String updateBy;
    @Schema(description = "备注")
    protected String remark;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
