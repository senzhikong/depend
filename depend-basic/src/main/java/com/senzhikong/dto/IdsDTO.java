package com.senzhikong.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "基础ID数组")
public class IdsDTO implements Serializable {
    @Schema(description = "ID")
    private Long[] id;
}
