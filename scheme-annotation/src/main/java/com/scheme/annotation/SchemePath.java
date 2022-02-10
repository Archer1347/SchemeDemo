package com.scheme.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * activity路由注解
 * Created by ljq on 2019/5/8
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.CLASS)
public @interface SchemePath {

    String value();
}
