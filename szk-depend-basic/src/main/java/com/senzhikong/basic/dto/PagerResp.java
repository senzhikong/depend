package com.senzhikong.basic.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author shu
 */
@Data
@Schema(name = "基础分页返回参数")
public class PagerResp<T> implements Serializable {
    @Schema(description = "页码", example = "1")
    private Long pageNumber;
    @Schema(description = "页码", example = "10")
    private Long pageSize;
    @Schema(description = "数据总条数", example = "999")
    private Long total;
    @Schema(description = "总页数", example = "8")
    private Long totalPage;
    @Schema(description = "数据列表")
    private List<T> dataList;

    public PagerResp() {

    }

    public static <K> PagerResp<K> build(PagerResp<?> pagerResp, List<K> dataList) {
        return new PagerResp<>(pagerResp, dataList);
    }

    public PagerResp(PagerResp<?> page, List<T> dataList) {
        this.setTotal(page.getTotal());
        this.setPageNumber(page.getPageNumber());
        this.setPageSize(page.getPageSize());
        this.setTotalPage(page.getTotalPage());
        this.dataList = dataList;
    }
}
