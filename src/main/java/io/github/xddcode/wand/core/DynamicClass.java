package io.github.xddcode.wand.core;

import io.github.xddcode.wand.exception.LoaderRuntimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:32
 */
@Slf4j
public class DynamicClass {
    private SourceType sourceType;
    private String javaFilePath;
    @Getter
    private String fullClassName;
    private List<String> options;
    private MemFileManager fileManager;

    public DynamicClass(String javaFilePath, String fullClassName) {
        this.sourceType = SourceType.Local;
        this.javaFilePath = javaFilePath;
        this.fullClassName = fullClassName;
    }

    public DynamicClass(SourceType sourceType, String javaFilePath, String fullClassName) {
        this.sourceType = sourceType;
        this.javaFilePath = javaFilePath;
        this.fullClassName = fullClassName;
    }

    public static DynamicClass init(String javaFilePath, String fullClassName) {
        log.debug("初始化class javaFilePath:{} fullClassName:{}", javaFilePath, fullClassName);
        return new DynamicClass(javaFilePath, fullClassName);
    }

    public static DynamicClass init(SourceType sourceType, String javaFilePath, String fullClassName) {
        log.debug("初始化class javaFilePath:{} fullClassName:{}", javaFilePath, fullClassName);
        return new DynamicClass(sourceType, javaFilePath, fullClassName);
    }

    public DynamicClass config(List<String> options) {
        log.debug("添加编译配置 options：{}", String.join(" ", options));
        this.options = options;
        return this;
    }

    /**
     * 编译配置classpath
     *
     * @param classpath
     * @return
     */
    public DynamicClass addClasspath(String classpath) {
        log.debug("添加编译配置 classpath：{}", classpath);
        options.add("-classpath");
        options.add(classpath);
        return this;
    }

    public DynamicClass compiler() {
        log.debug("开始执行编译");
        // 获取Java编译器
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        // 创建诊断收集器
        DiagnosticCollector<JavaFileObject> diagnosticCollector = new DiagnosticCollector<>();
        // 创建内存文件管理器
        this.fileManager = new MemFileManager(compiler.getStandardFileManager(diagnosticCollector, null, null));
        // 创建JavaSource对象
        AbstractSource file = new JavaMemSource(fullClassName, javaFilePath);
        if (sourceType == SourceType.AliOSS) {
            file = new JavaOssSource(javaFilePath);
        }
        // 创建编译单元Iterable
        Iterable<? extends JavaFileObject> compilationUnits = Collections.singletonList(file);
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnosticCollector, options, null, compilationUnits);
        boolean result = task.call();
        if (!result) {
            // 处理编译错误
            String errorMessage = diagnosticCollector.getDiagnostics().stream()
                    .map(Object::toString)
                    .reduce("", (acc, x) -> acc + "\r\n" + x);
            log.debug("编译失败: {}", errorMessage);
            throw new LoaderRuntimeException("Compilation failed due to syntax errors or incorrect classpath settings.");
        }
        log.debug("编译成功");
        return this;
    }

    public Class<?> load() {
        try {
            Map<String, byte[]> compiledClasses = fileManager.getAllCompiledClassesData();
            DynamicClassLoader classLoader = new DynamicClassLoader(Thread.currentThread().getContextClassLoader());
            compiledClasses.forEach(classLoader::addClass);
            return classLoader.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            throw new LoaderRuntimeException("Failed to load the compiled class. The class file may not have been generated correctly or cannot be found in the specified classpath.");
        }
    }
}
