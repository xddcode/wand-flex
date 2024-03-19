package io.github.xddcode.wand.springboot;

import io.github.xddcode.wand.core.config.IScannerRunner;
import io.github.xddcode.wand.core.config.OssConfig;
import io.github.xddcode.wand.core.datasource.DatasourceManager;
import io.github.xddcode.wand.core.expose.ExposeContext;
import io.github.xddcode.wand.core.utils.SpringContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:53
 */
@Configuration
@EnableConfigurationProperties(OssConfig.class)
public class IAutoConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean(name = "loaderUtilSpringContextUtils")
    public SpringContextUtils springContextUtils() {
        return new SpringContextUtils();
    }

    @Bean
    public IScannerRunner iScannerRunner() {
        return new IScannerRunner();
    }

    @Bean
    public DatasourceManager datasourceManager() {
        return new DatasourceManager(dataSource);
    }

    @Bean
    public ExposeContext exposeContext() {
        return new ExposeContext(datasourceManager());
    }

}
