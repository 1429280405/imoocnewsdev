package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;

/**
 * @author liujq
 * @create 2021-08-19 9:48
 */
public class GraceException extends RuntimeException {

    public static void display(ResponseStatusEnum smsCodeError) {
        throw new MyCustomException(smsCodeError);
    }
}
