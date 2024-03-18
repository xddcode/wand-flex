package io.github.xddcode.wand.core;

import lombok.Getter;

import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:33
 */
@Getter
public class MemFileManager extends ForwardingJavaFileManager<StandardJavaFileManager> {

    private final Map<String, JavaMemClass> compiledClasses = new HashMap<>();

    public MemFileManager(StandardJavaFileManager fileManager) {
        super(fileManager);
    }

    @Override
    public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
        // 这里创建的JavaMemClass实例会被JavaCompiler用来存储编译好的类的字节码数据
        JavaMemClass javaMemClass = new JavaMemClass(className, kind);
        // 存储编译好的类
        compiledClasses.put(className, javaMemClass);
        return javaMemClass;
    }

    /**
     * 获取所有编译好的类的字节码数据
     *
     * @return
     */
    public Map<String, byte[]> getAllCompiledClassesData() {
        Map<String, byte[]> classDataMap = new HashMap<>();
        for (Map.Entry<String, JavaMemClass> entry : compiledClasses.entrySet()) {
            // 将每个编译好的类的字节码数据存入Map中
            classDataMap.put(entry.getKey(), entry.getValue().getBytes());
        }
        return classDataMap;
    }
}
