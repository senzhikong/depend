package com.senzhikong.basic.dto;

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
public class PagerReq<T> extends PagerParam implements Serializable {

    @Schema(description = "筛选字段")
    private T query;

}
