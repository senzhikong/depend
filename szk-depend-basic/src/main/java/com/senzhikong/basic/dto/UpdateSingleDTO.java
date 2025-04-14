package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "更新单个参数请求参数")
public class UpdateSingleDTO implements Serializable {
    @NotBlank(message = "{validate.base:id.notBlank}")
    @Schema(description = "ID")
    private String id;
    @NotBlank(message = "{validate.base:key.notBlank}")
    @Schema(description = "修改键")
    private String key;
    @NotBlank(message = "{validate.base:value.notBlank}")
    @Schema(description = "修改值")
    private String value;
}
