package com.sky.exception;

public class UserHasBeenRegisteredException extends BaseException{
    public UserHasBeenRegisteredException() {
    }

    public UserHasBeenRegisteredException(String msg) {
        super(msg);
    }
}
