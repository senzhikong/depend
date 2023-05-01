package com.senzhikong.web.excepition;

import com.senzhikong.exception.AuthException;
import com.senzhikong.exception.DataException;
import com.senzhikong.web.ajax.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

/**
 * @author shu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DataException.class)
    public ApiResponse<Object> dataError(HttpServletResponse response, DataException error) {
        return ApiResponse.dataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ApiResponse<Object> authError(HttpServletResponse response, AuthException error) {
        return ApiResponse.dataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> authError(HttpServletResponse response, Exception error) {
        log.error("系统异常", error);
        return ApiResponse.dataError("系统异常");
    }
}
