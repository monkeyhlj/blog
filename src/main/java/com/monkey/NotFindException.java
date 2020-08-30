package com.monkey;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND) //指定状态（ControllerExceptionHandler中找到并转向）
public class NotFindException extends RuntimeException{ //在运行过程中捕获异常

    public NotFindException() {
    }

    public NotFindException(String message) {
        super(message);
    }

    public NotFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
