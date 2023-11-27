package com.sky.exception;

/**
 * 注册时字段为空
 */
public class RegisterNullException extends BaseException{
    public RegisterNullException() {
    }

    public RegisterNullException(String msg) {
        super(msg);
    }
}
