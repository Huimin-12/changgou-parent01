package com.changgou.service.goods.exception;

import com.changgou.service.goods.util.EResultCode;

/*
 * 自定义异常类*/
public class MyBusinessException extends RuntimeException {
    //调用枚举
    private EResultCode eResultCode;
    //有参构造器
    public MyBusinessException(EResultCode eResultCode) {
        this.eResultCode = eResultCode;
    }

    public EResultCode geteResultCode() {
        return eResultCode;
    }

    public static MyBusinessException throwE(EResultCode eResultCode) {
        //调用有参
        throw  new MyBusinessException(eResultCode);
    }

}
