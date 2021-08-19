package com.imooc.pojo.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author liujq
 * @create 2021-08-19 10:38
 */
@Data
public class RegistLoginBO {
    @NotBlank(message = "手机号不能为空！")
    private String mobile;
    @NotBlank(message = "验证码不能为空")
    private String smsCode;


}
