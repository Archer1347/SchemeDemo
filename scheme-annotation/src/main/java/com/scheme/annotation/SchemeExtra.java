package com.scheme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * activity参数注解
 * Created by ljq on 2020/7/14
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.CLASS)
public @interface SchemeExtra {

    String value() default "";

    boolean required() default false;

    String desc() default "";
}
