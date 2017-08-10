package com.twork.controller;

import com.twork.util.Code;
import com.twork.vo.Result;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    public @ResponseBody
    Result handleException(Exception exception) {
        Result result = new Result();
        if (exception instanceof HttpSessionRequiredException){
            System.out.println("*** ExceptionHandler HttpSessionRequiredException: ***");
            System.err.println(exception);
            System.out.println();
            exception.printStackTrace();
            result.setCode(Code.NOLOGIN);
            return result;
        }

        if (exception instanceof MissingServletRequestParameterException){
            System.out.println("*** ExceptionHandler MissingServletRequestParameterException: ***");
            System.err.println(exception);
            System.out.println();
            exception.printStackTrace();
            result.setCode(Code.PARAMETER_NOT_MATCH);
            return result;
        }

        if (exception instanceof HttpRequestMethodNotSupportedException){
            System.out.println("*** ExceptionHandler HttpRequestMethodNotSupportedException: ***");
            System.err.println(exception);
            System.out.println();
            exception.printStackTrace();
            result.setCode(Code.HTTP_REQUEST_METHOD_NOT_SUPPORTED);
            return result;
        }

        System.out.println("*** ExceptionHandler: ***");
        System.err.println(exception);
        System.out.println();
        exception.printStackTrace();
        result.setCode(Code.UNKNOWN_ERROR);
        return result;
    }
}
