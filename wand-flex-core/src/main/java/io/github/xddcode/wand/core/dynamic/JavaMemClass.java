package io.github.xddcode.wand.core.dynamic;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:33
 */
public class JavaMemClass extends SimpleJavaFileObject {

    protected final ByteArrayOutputStream classByteArrayOutputStream = new ByteArrayOutputStream();

    public JavaMemClass(String name, Kind kind) {
        super(URI.create("string:///" + name.replace('.', '/')
                + kind.extension), kind);
    }

    /**
     * 获取字节数组
     *
     * @return 字节数组
     */
    public byte[] getBytes() {
        return classByteArrayOutputStream.toByteArray();
    }


    /**
     * 重写openOutputStream方法
     *
     * @return 返回classByteArrayOutputStream对象
     */
    @Override
    public OutputStream openOutputStream() {
        return classByteArrayOutputStream;
    }
}
