package io.github.xddcode.wand.config;

import lombok.Data;

/**
 * 数据源配置
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/18 10:51
 */
@Data
public class DataSourceConfig {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
}
