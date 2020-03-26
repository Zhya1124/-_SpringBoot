package com.wldemo.demo.exception;

public class CustomizeException extends RuntimeException {   //这样不用在throwException的时候必须trycatch
    private String message;
    private Integer code;

    public CustomizeException(ICustomizeErrorCode errorCode) {//从大接口上拿，到时候传的时候传实现类
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
