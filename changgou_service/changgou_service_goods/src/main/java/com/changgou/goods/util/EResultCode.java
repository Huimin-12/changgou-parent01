package com.changgou.service.goods.util;

/*
* 封装异常信息
* 枚举*/

public enum  EResultCode {

    TIMEOUT_(100,"网络异常"),
    PARAM_ERROR(101,"参数不合法"),
    DATA_NOT_EXIST(102,"数据不存在");

    private  Integer code;
    private  String desc;

    private EResultCode(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public Integer getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
