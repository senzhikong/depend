package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@ApiModel("基础ID")
public class IdDTO {
    @Schema(description = "ID")
    private Long id;
}
