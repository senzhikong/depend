package com.senzhikong.web.excepition;

import com.senzhikong.basic.dto.ApiResp;
import com.senzhikong.basic.exception.AuthException;
import com.senzhikong.basic.exception.DataException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author shu
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(DataException.class)
    public ApiResp<Object> dataError(DataException error) {
        ApiResp<Object> res = ApiResp.error(error.getMessage());
        if (error.getCode() != null) {
            res.setCode(error.getCode());
        }
        return res;
    }

    @ResponseBody
    @ExceptionHandler(AuthException.class)
    public ApiResp<Object> authError(AuthException error) {
        return ApiResp.error(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(NoResourceFoundException.class)
    public ApiResp<Object> noResourceError(NoResourceFoundException error) {
        return ApiResp.error(error.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResp<Object> argError(MethodArgumentNotValidException error) {
        List<String> errs = new ArrayList<>();
        error.getFieldErrors().forEach((err) -> errs.add("参数【".concat(err.getField()).concat("】").concat(Objects.requireNonNull(err.getDefaultMessage()))));
        return ApiResp.error(StringUtils.join(errs, ","));
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ApiResp<Object> sysError(Exception error) {
        log.error("系统异常", error);
        return ApiResp.error("系统异常");
    }
}
