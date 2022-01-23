package com.senzhikong.web.excepition;

import com.senzhikong.exception.AuthError;
import com.senzhikong.exception.DataError;
import com.senzhikong.web.ajax.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DataError.class)
    public ApiResponse<Object> dataError(HttpServletResponse response, DataError error) {
        return ApiResponse.DataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthError.class)
    public ApiResponse<Object> authError(HttpServletResponse response, AuthError error) {
        return ApiResponse.DataError(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResponse<Object> authError(HttpServletResponse response, Exception error) {
        log.error("系统异常",error);
        return ApiResponse.DataError("系统异常");
    }
}
