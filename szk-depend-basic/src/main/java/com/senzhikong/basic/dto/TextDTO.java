package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "基础文本")
public class TextDTO implements Serializable {
    @NotBlank(message = "{validate.base:text.notBlank}")
    @Schema(description = "text")
    private String text;
}
