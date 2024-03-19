package io.github.xddcode.wand.config;

import io.github.xddcode.wand.datasource.DatasourceManager;
import io.github.xddcode.wand.expose.ExposeContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:53
 */
@AutoConfiguration
@EnableConfigurationProperties(OssConfig.class)
public class IAutoConfiguration {

    @Autowired
    private DataSource dataSource;

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
