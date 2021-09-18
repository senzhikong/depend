package com.senzhikong.web.ajax;

import com.alibaba.fastjson.JSONObject;
import com.senzhikong.db.sql.wrapper.PagerQueryWrapper;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * json返回实体类
 *
 * @author Shu.zhou
 */
@Data
public class PagerResponse<T> implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    // 返回错误编码
    private int code = ApiStatus.OK.value();
    // 返回错误信息
    private String message = ApiStatus.OK.message();
    // 返回数据,如果出错返回null
    private List<T> data;
    private Long count;
    private Integer limit;
    private Integer curr;

    /**
     * 默认成功
     */
    public PagerResponse() {
        this(ApiStatus.OK);
    }

    public PagerResponse(PagerQueryWrapper<?> pagerQueryWrapper) {
        this.setCount(pagerQueryWrapper.getTotal());
        this.setLimit(pagerQueryWrapper.getPageSize());
        this.setCurr(pagerQueryWrapper.getPageNumber());
    }

    public PagerResponse(PagerQueryWrapper<?> pagerQueryWrapper, List<T> list) {
        this(pagerQueryWrapper);
        this.data = list;
    }

    public PagerResponse(ApiStatus status) {
        this.setCode(status.value());
        this.setMessage(status.message());
    }

    public PagerResponse(int code, String message, List<T> data) {
        this();
        this.setCode(code);
        this.setMessage(message);
        this.setData(data);
    }

    public PagerResponse(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public static PagerResponse<Object> ok(String message) {
        return new PagerResponse<>(ApiStatus.OK.value(), message);
    }

    public static <T> PagerResponse<T> ok(List<T> data) {
        return new PagerResponse<>(ApiStatus.OK.value(), ApiStatus.OK.message(), data);
    }

    public static <T> PagerResponse<T> ok(String message, List<T> data) {
        return new PagerResponse<>(ApiStatus.OK.value(), message, data);
    }

    public static PagerResponse<Object> ok() {
        return new PagerResponse<>(ApiStatus.OK.value(), ApiStatus.OK.message());
    }

    public static PagerResponse<Object> error(String message) {
        return new PagerResponse<>(ApiStatus.ERROR.value(), message);
    }

    public static <T> PagerResponse<T> error(List<T> data) {
        return new PagerResponse<>(ApiStatus.ERROR.value(), ApiStatus.ERROR.message(), data);
    }

    public static <T> PagerResponse<T> error(String message, List<T> data) {
        return new PagerResponse<>(ApiStatus.ERROR.value(), message, data);
    }

    public static <T> PagerResponse<T> error() {
        return new PagerResponse<>(ApiStatus.ERROR.value(), ApiStatus.ERROR.message());
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
