package io.github.xddcode.wand.expose;

import io.github.xddcode.wand.expose.enums.ExposeType;
import lombok.Data;

import java.util.List;

/**
 * ExposeInfo
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 14:06
 */
@Data
public class ExposeInfo {
    private String name;
    private String description;
    private ExposeType type;
    private List<Method> methods;

    @Data
    public static class Method {
        private String name;
        private String returnType;
        private String description;
        private List<Parameter> parameters;
    }

    @Data
    public static class Parameter {
        private String name;
        private String dataType;
        private String description;
    }
}
