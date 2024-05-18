package com.senzhikong.dto;

import com.senzhikong.db.sql.wrapper.PagerQueryWrapper;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.List;

/**
 * @author shu
 */
@Data
@Schema(name = "基础分页返回参数")
public class PagerResponseDTO<T> implements Serializable {
    @Schema(description = "页码", example = "1")
    private Integer pageNumber;
    @Schema(description = "页码", example = "10")
    private Integer pageSize;
    @Schema(description = "数据总条数", example = "999")
    private Long total;
    @Schema(description = "总页数", example = "8")
    private Integer totalPage;
    @Schema(description = "数据列表")
    private List<T> dataList;

    public void setPage(Page<?> page) {
        this.setTotal(page.getTotalElements());
        this.setPageNumber(page.getNumber());
        this.setPageSize(page.getSize());
        this.setTotalPage(page.getTotalPages());
    }


    public void setWrapper(PagerQueryWrapper<?> wrapper) {
        this.setTotal(wrapper.getTotal());
        this.setPageNumber(wrapper.getPageNumber());
        this.setPageSize(wrapper.getPageSize());
        this.setTotalPage(wrapper.getTotalPages());
    }
}
