package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@ApiModel("基础对象模型")
public class BaseEntityDTO implements Serializable {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "状态")
    private String status;
    @Schema(description = "状态")
    private String statusDesc;
    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "创建人")
    private Long createBy;
    @Schema(description = "更新时间")
    private Date updateTime;
    @Schema(description = "更新人")
    private Long updateBy;
    @Schema(description = "备注")
    private String remark;
}
