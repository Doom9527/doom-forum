package com.sky.exception;

public class UserNameOrPasswordErrorException extends BaseException{
    public UserNameOrPasswordErrorException() {
    }

    public UserNameOrPasswordErrorException(String msg) {
        super(msg);
    }
}
