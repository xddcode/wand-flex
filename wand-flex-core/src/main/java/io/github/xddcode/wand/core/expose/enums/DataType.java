package io.github.xddcode.wand.core.expose.enums;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 14:07
 */
public enum DataType {
    INTEGER("int"),
    STRING("String"),
    BOOLEAN("boolean"),
    DOUBLE("double"),
    LONG("long"),
    FLOAT("float"),
    BYTE("byte"),
    CHAR("char"),
    SHORT("short"),
    OBJECT("Object");

    private final String type;

    DataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * 获取枚举实例对应的类型名称。
     */
    @Override
    public String toString() {
        return this.type;
    }

    /**
     * 根据类型名称返回对应的枚举实例。如果找不到匹配项，可以返回 null 或抛出异常。
     *
     * @param type 类型名称
     * @return 对应的枚举实例
     */
    public static DataType fromString(String type) {
        for (DataType dataType : DataType.values()) {
            if (dataType.type.equalsIgnoreCase(type)) {
                return dataType;
            }
        }
        return STRING;
    }
}
