package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "更新状态请求参数")
public class UpdateStatusDTO implements Serializable {
    @NotBlank(message = "{validate.base:id.notBlank}")
    @Schema(description = "ID")
    private String[] id;

    @NotBlank(message = "{validate.base:status.notBlank}")
    @Schema(description = "状态")
    private String status;
}
