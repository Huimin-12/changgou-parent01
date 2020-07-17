package com.changgou.goods.handler;

import com.changgou.entity.Result;
import com.changgou.entity.StatusCode;
import com.changgou.service.goods.exception.MyBusinessException;
import com.changgou.service.goods.util.EResultCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理类
 */
@ControllerAdvice //声明该类是一个增强类
public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e){
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,"当前系统繁忙,请您稍后重试");
    }
    @ExceptionHandler(value = MyBusinessException.class)
    @ResponseBody
    public Result businessError(MyBusinessException e){
        //打印日志
        EResultCode eResultCode = e.geteResultCode();
        return new Result(false, eResultCode.getCode(),eResultCode.getDesc());
    }
}
