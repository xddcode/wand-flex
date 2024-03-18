package io.github.xddcode.wand.core;

import java.security.SecureClassLoader;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态类加载器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:32
 */
public class DynamicClassLoader extends SecureClassLoader {

    /**
     * 编译的时候返回的class字节数组-支持内部类
     */
    private final Map<String, byte[]> classBytes = new HashMap<>();

    public DynamicClassLoader(ClassLoader parent) {
        super(parent);
    }

    public void addClass(String fullClassName, byte[] classData) {
        classBytes.put(fullClassName, classData);
    }

    @Override
    protected Class<?> findClass(String fullClassName) throws ClassNotFoundException {
        byte[] classData = classBytes.get(fullClassName);
        if (classData == null) {
            throw new ClassNotFoundException("Failed to dynamically load the class[" + fullClassName + "]. The class data could not be found in the compiled classes cache.");
        }
        return defineClass(fullClassName, classData, 0, classData.length);
    }
}
