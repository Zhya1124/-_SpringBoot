package com.wldemo.demo.exception;

public interface ICustomizeErrorCode {  //定义成接口以便引用使用，因为以后会有很多不同场景的errorCode
    String getMessage();
    Integer getCode();
}
