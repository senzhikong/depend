package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("基础ID")
public class IdDTO implements Serializable {
    @Schema(description = "ID")
    private Long id;

    public static IdDTO form(Long id) {
        IdDTO dto = new IdDTO();
        dto.setId(id);
        return dto;
    }
}
