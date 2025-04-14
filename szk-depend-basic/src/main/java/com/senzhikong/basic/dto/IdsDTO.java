package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shu
 */
@Data
@Schema(name = "基础ID数组")
public class IdsDTO implements Serializable {
    @NotBlank(message = "{validate.base:id.notBlank}")
    @Schema(description = "ID")
    private List<String> id;
}
