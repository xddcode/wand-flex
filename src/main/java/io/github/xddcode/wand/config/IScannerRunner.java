package io.github.xddcode.wand.config;

import io.github.xddcode.wand.expose.DependencySupport;
import io.github.xddcode.wand.expose.ExposeMethodSupport;
import io.github.xddcode.wand.expose.annotation.EnableExposeMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

/**
 * 自定义启动扫描器
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 14:38
 */
@Slf4j
public class IScannerRunner implements CommandLineRunner {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void run(String... args) throws Exception {
        // 获取启动类，初始化扫描词包内所有携带特定注解的方法
        String[] sourceNames = applicationContext.getBeanNamesForAnnotation(SpringBootApplication.class);
        if (sourceNames.length > 0) {
            Class<?> bootstrapClass = applicationContext.getType(sourceNames[0]);
            if (bootstrapClass == null) {
                return;
            }
            EnableExposeMethod annotation = AnnotationUtils.findAnnotation(bootstrapClass, EnableExposeMethod.class);
            if (annotation != null) {
                String basePackage = annotation.scannerPackage();
                ExposeMethodSupport.init(basePackage);
            }
        }
        //初始化扫描pom依赖列表
        DependencySupport.init();
    }
}
