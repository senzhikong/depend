package com.senzhikong.basic.dto;

import com.alibaba.fastjson.JSON;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shu
 */
@Data
@Schema(name = "基础分页参数")
public class PagerParam implements Serializable {

    @Schema(description = "页码", example = "1")
    private Integer pageNumber;
    @Schema(description = "页码", example = "10")
    private Integer pageSize;
    @Schema(description = "是否分页", example = "true")
    private Boolean pageable;
    @Schema(description = "排序类型", example = "id")
    private String orderBy;
    @Schema(description = "排序方式(ASC或DESC)", example = "ASC")
    private String orderType;
    @Schema(description = "搜索关键字", example = "key")
    private String keyword;

    public PagerParam() {
        this.pageNumber = 1;
        this.pageSize = 10;
        this.pageable = true;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
