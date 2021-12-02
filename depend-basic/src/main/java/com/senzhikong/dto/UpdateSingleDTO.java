package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("更新单个参数请求参数")
public class UpdateSingleDTO implements Serializable {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "修改键")
    private String key;
    @Schema(description = "修改值")
    private String value;
}
