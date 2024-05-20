package com.senzhikong.web.excepition;

import com.senzhikong.basic.dto.ApiResp;
import com.senzhikong.basic.exception.AuthException;
import com.senzhikong.basic.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.resource.NoResourceFoundException;

/**
 * @author shu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DataException.class)
    public ApiResp<Object> dataError(DataException error) {
        return ApiResp.error(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ApiResp<Object> authError(AuthException error) {
        return ApiResp.error(error.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(WebExchangeBindException.class)
    public ApiResp<Object> argError(WebExchangeBindException error) {
        BindingResult result = error.getBindingResult();
        StringBuilder msg = new StringBuilder();
        if (result.getFieldErrorCount() > 0) {
            for (FieldError fieldErr : result.getFieldErrors()) {
                msg.append(fieldErr.getField()).append(":").append(fieldErr.getDefaultMessage()).append("|");
            }
            msg.deleteCharAt(msg.length() - 1);
        }
        return ApiResp.error(msg.toString());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResp<Object> sysError(Exception error) {
        log.error("系统异常", error);
        return ApiResp.error("系统异常");
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResp<Object> noResourceError(NoResourceFoundException error) {
        return ApiResp.error(error.getMessage());
    }
}
