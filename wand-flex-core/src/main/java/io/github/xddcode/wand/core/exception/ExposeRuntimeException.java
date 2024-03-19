package io.github.xddcode.wand.core.exception;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/14 16:27
 */
public class ExposeRuntimeException extends RuntimeException {
    public ExposeRuntimeException(String message) {
        super(message);
    }

    public ExposeRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}