package io.github.xddcode.wand.utils;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一返回结果WandResponse
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/15 10:35
 */
@Data
public class WandResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String errMessage;

    private Object data;

    public static WandResponse success() {
        WandResponse result = new WandResponse();
        result.setCode(0);
        return result;
    }

    public static WandResponse success(Object data) {
        WandResponse result = new WandResponse();
        result.setCode(0);
        result.setData(data);
        return result;
    }

    // 返回失败
    public static WandResponse fail(Integer code, String message) {
        WandResponse result = new WandResponse();
        result.setCode(code);
        result.setErrMessage(message);
        return result;
    }

    // 返回失败
    public static WandResponse fail(String message) {
        WandResponse result = new WandResponse();
        result.setCode(-1);
        result.setErrMessage(message);
        return result;
    }
}
