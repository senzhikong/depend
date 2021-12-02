package com.senzhikong.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel("基础分页请求参数")
public class PagerRequestDTO implements Serializable {

    @Schema(description = "页码", example = "1")
    private Integer pageNumber = 1;
    @Schema(description = "页码", example = "10")
    private Integer pageSize = 10;
    @Schema(description = "是否分页", example = "true")
    private boolean isPage = true;
    @Schema(description = "排序类型", example = "id")
    private String orderBy;
    @Schema(description = "排序方式", example = "asc")
    private String orderType;
    @Schema(description = "搜索关键字", example = "key")
    private String search;
    @Schema(description = "状态", example = "normal")
    private String status;

}
