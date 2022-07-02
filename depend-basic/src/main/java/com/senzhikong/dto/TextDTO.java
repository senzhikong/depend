package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
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
@ApiModel("基础文本")
public class TextDTO implements Serializable {
    @Schema(description = "text")
    private String text;
}
