package com.imooc.exception;

import com.imooc.grace.result.ResponseStatusEnum;
import lombok.Data;

/**
 * @author liujq
 * @create 2021-08-19 9:57
 */
@Data
public class MyCustomException extends RuntimeException {

    private ResponseStatusEnum smsCodeError;

    public MyCustomException(ResponseStatusEnum smsCodeError) {
        super("异常状态码："+smsCodeError.status()+"；具体异常信息为："+smsCodeError.msg());
        this.smsCodeError=smsCodeError;
    }


}
