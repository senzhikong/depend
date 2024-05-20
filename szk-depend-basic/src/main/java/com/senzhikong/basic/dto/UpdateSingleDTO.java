package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "更新单个参数请求参数")
public class UpdateSingleDTO implements Serializable {
    @Schema(description = "ID")
    private Long id;
    @Schema(description = "修改键")
    private String key;
    @Schema(description = "修改值")
    private String value;
}
