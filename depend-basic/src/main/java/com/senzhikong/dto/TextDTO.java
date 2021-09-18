package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel("基础文本")
public class TextDTO {
    @Schema(description = "text")
    private String text;

    public TextDTO(String text){
        this.text=text;
    }
}
