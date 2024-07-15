package com.bonc.plugin.agent.util;

import com.alibaba.fastjson.JSON;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author：nihongyu
 * @date: 2024/6/7
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 这只针对实体字段非空判断的统一返回
     * @param ex
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResultObject handleValidationExceptions(MethodArgumentNotValidException ex) {
        StringBuilder builder=new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            builder.append(fieldName);
            builder.append(errorMessage);
            builder.append(";");
        });
        return ResultObject.error(builder.toString());
    }

}
