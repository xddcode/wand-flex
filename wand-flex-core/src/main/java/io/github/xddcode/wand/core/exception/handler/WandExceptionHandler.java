package io.github.xddcode.wand.core.exception.handler;

import io.github.xddcode.wand.core.exception.DataSourceException;
import io.github.xddcode.wand.core.exception.ExposeRuntimeException;
import io.github.xddcode.wand.core.exception.LoaderRuntimeException;
import io.github.xddcode.wand.core.utils.WandResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2023/11/21 18:14
 */
@Slf4j
@RestControllerAdvice
public class WandExceptionHandler {

    @ExceptionHandler(LoaderRuntimeException.class)
    public WandResponse handleException(LoaderRuntimeException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return WandResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(ExposeRuntimeException.class)
    public WandResponse handleException(ExposeRuntimeException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return WandResponse.fail(ex.getMessage());
    }

    @ExceptionHandler(DataSourceException.class)
    public WandResponse handleException(DataSourceException ex) {
        log.error(ex.getMessage(), ex.getCause());
        return WandResponse.fail(ex.getMessage());
    }
}