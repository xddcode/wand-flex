package io.github.xddcode.wand.core.exception;

/**
 * 数据源异常
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 11:35
 */
public class DataSourceException extends RuntimeException {
    public DataSourceException(String message) {
        super(message);
    }

    public DataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}