package io.github.xddcode.wand.core.dynamic;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import io.github.xddcode.wand.core.config.OssConfig;
import io.github.xddcode.wand.core.exception.LoaderRuntimeException;
import io.github.xddcode.wand.core.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.stream.Collectors;

/**
 * 从OSS下载Java源代码文件的JavaFileObject。
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/21 10:18
 */
@Slf4j
public class JavaOssSource extends AbstractSource {
    private String javaFilePath;
    private OSS ossClient;
    private String bucket;

    /**
     * 构造函数，指定远程文件的URI和文件类型（Kind.SOURCE代表Java源代码文件）。
     */
    public JavaOssSource(String javaFilePath) {
        super(URI.create(javaFilePath), Kind.SOURCE);
        this.javaFilePath = javaFilePath;
        try {
            OssConfig config = SpringContextUtils.getBean(OssConfig.class);
            this.ossClient = new OSSClientBuilder().build(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret());
            this.bucket = config.getBucket();
        } catch (Exception e) {
            throw new LoaderRuntimeException("Failed to create OSS client: " + e.getMessage());
        }
    }

    @Override
    public String sourceContent() {
        log.debug("加载OSS中的java源码文件: {}", javaFilePath);
        validUrl(javaFilePath);
        try {
            int index = javaFilePath.indexOf('/');
            int nextSlashIndex = javaFilePath.indexOf('/', index + 2);
            String path = javaFilePath.substring(nextSlashIndex + 1);
            OSSObject ossObject = ossClient.getObject(bucket, path);
            try (InputStream content = ossObject.getObjectContent();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(content))) {
                return reader.lines().collect(Collectors.joining("\n"));
            }
        } catch (Exception e) {
            throw new LoaderRuntimeException("Failed to read Java source file from OSS: " + javaFilePath);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    private void validUrl(String url) {
        try {
            new URL(url);
        } catch (MalformedURLException e) {
            throw new LoaderRuntimeException("Invalid URL: " + url, e);
        }
    }
}
