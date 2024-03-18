package io.github.xddcode.wand.config;

import io.github.xddcode.wand.expose.DependencySupport;
import io.github.xddcode.wand.expose.ExposeMethodSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;

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
        //初始化扫描pom依赖列表
        DependencySupport.init();
        // 获取应用基础包名，或者选择一个合适的包名
        String basePackage = applicationContext.getApplicationName();
        ExposeMethodSupport.init(basePackage);
    }
}
