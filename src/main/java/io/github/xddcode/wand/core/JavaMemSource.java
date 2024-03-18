package io.github.xddcode.wand.core;


import io.github.xddcode.wand.exception.LoaderRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:34
 */
@Slf4j
public class JavaMemSource extends AbstractSource {

    /**
     * java源码
     */
    private String javaFilePath;

    public JavaMemSource(String name, String javaFilePath) {
        super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.javaFilePath = javaFilePath;
    }

    @Override
    public String sourceContent() {
        try {
            log.debug("加载内存中的java源码文件: {}", javaFilePath);
            return Files.readString(Paths.get(javaFilePath));
        } catch (IOException e) {
            throw new LoaderRuntimeException("Failed to read Java source file from local file system: " + javaFilePath);
        }
    }
}
