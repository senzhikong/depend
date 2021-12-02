package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("更新状态请求参数")
public class UpdateStatusDTO implements Serializable {
    @Schema(description = "ID")
    private Long[] id;

    @Schema(description = "状态")
    private String status;
}
