package com.senzhikong.web.excepition;

import com.senzhikong.basic.exception.AuthException;
import com.senzhikong.basic.exception.DataException;
import com.senzhikong.web.ajax.ApiResp;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DataException.class)
    public ApiResp<Object> dataError(HttpServletResponse response, DataException error) {
        return ApiResp.dataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ApiResp<Object> authError(HttpServletResponse response, AuthException error) {
        return ApiResp.dataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResp<Object> authError(HttpServletResponse response, Exception error) {
        log.error("系统异常", error);
        return ApiResp.dataError("系统异常");
    }
}
