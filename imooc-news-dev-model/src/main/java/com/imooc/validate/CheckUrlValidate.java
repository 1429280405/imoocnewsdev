package com.imooc.validate;

import com.imooc.utils.UrlUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author liujinqiang
 * @create 2021-08-29 11:18
 */
public class CheckUrlValidate implements ConstraintValidator<CheckUrl, String> {
    public void initialize(CheckUrl constraint) {
    }

    public boolean isValid(String url, ConstraintValidatorContext context) {
        return UrlUtil.verifyUrl(url.trim());
    }
}
