package io.github.xddcode.wand.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/23 16:29
 */
@Data
@ConfigurationProperties(prefix = "wand.oss")
public class OssConfig {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
}
