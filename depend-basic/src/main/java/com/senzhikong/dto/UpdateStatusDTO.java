package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel("更新状态请求参数")
public class UpdateStatusDTO {
    @Schema(description = "ID")
    private Long[] id;

    @Schema(description = "状态")
    private String status;
}
