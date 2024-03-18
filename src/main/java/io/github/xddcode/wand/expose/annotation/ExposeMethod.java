package io.github.xddcode.wand.expose.annotation;

import io.github.xddcode.wand.expose.enums.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:34
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeMethod {

    ExposeParameter[] parameters() default {};

    DataType returnType() default DataType.STRING;

    String description() default "";

}
