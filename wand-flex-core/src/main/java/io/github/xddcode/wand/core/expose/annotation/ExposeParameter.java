package io.github.xddcode.wand.core.expose.annotation;

import io.github.xddcode.wand.core.expose.enums.DataType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 13:36
 */
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExposeParameter {

    String name() default "";

    DataType dataType() default DataType.STRING;

    String description() default "";
}
