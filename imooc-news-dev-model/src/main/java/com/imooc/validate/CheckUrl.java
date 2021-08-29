package com.imooc.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author liujinqiang
 * @create 2021-08-29 11:15
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckUrlValidate.class)
public @interface CheckUrl {
    String message() default "Url不正确";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
