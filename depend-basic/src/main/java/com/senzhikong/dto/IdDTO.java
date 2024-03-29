package com.senzhikong.dto;

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
@Schema(name = "基础ID")
public class IdDTO implements Serializable {
    @Schema(description = "ID")
    private Long id;

    public static IdDTO form(Long id) {
        IdDTO dto = new IdDTO();
        dto.setId(id);
        return dto;
    }
}
