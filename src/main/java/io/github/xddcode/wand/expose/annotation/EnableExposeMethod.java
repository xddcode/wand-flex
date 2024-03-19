package io.github.xddcode.wand.expose.annotation;

import java.lang.annotation.*;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:57
 */
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableExposeMethod {

    String scannerPackage();
}
