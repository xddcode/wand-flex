package io.github.xddcode.wand.core.expose.annotation;

import io.github.xddcode.wand.core.expose.enums.ExposeType;

import java.lang.annotation.*;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 12:01
 */
@Documented
@Target({ElementType.METHOD, ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Expose {
    String name() default "";

    String description() default "";

    ExposeType type() default ExposeType.SpringBean;
}
