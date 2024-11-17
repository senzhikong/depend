package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "更新状态请求参数")
public class UpdateStatusDTO implements Serializable {
    @Schema(description = "ID")
    private String[] id;

    @Schema(description = "状态")
    private String status;
}
