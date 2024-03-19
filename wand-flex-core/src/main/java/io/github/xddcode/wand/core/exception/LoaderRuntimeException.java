package io.github.xddcode.wand.core.exception;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:35
 */
public class LoaderRuntimeException extends RuntimeException {
    public LoaderRuntimeException(String message) {
        super(message);
    }

    public LoaderRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
