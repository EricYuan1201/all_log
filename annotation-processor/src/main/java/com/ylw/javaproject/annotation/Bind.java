package com.ylw.javaproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bind注解，用于标记需要设置ActionLog标签的视图
 * 例如：@Bind(id="O123")
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface Bind {
    /**
     * 标签ID
     */
    String id();
}
