package com.senzhikong.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "基础分页请求参数")
public class PagerRequestDTO extends BaseDTO implements Serializable {

    @Schema(description = "页码", example = "1")
    private Integer pageNumber;
    @Schema(description = "页码", example = "10")
    private Integer pageSize;
    @Schema(description = "是否分页", example = "true")
    private Boolean page;
    @Schema(description = "排序类型", example = "id")
    private String orderBy;
    @Schema(description = "排序方式", example = "asc")
    private String orderType;
    @Schema(description = "搜索关键字", example = "key")
    private String search;
    @Schema(description = "状态", example = "normal")
    private String status;

    public PagerRequestDTO() {
        this.pageNumber = 1;
        this.pageSize = 10;
        this.page = true;
    }
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
