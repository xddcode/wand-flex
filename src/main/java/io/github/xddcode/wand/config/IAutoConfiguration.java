package io.github.xddcode.wand.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:53
 */
@AutoConfiguration
@EnableConfigurationProperties(OssConfig.class)
public class IAutoConfiguration {

    @Bean
    public IScannerRunner customAnnotationBeanPostProcessor() {
        return new IScannerRunner();
    }
}
