package io.github.xddcode.wand.expose;

import io.github.xddcode.wand.datasource.TableInfo;
import lombok.Data;
import org.apache.maven.model.Dependency;

import java.util.List;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/19 11:23
 */
@Data
public class ExposeResource {
    private String groupId;
    private String artifactId;
    private String version;
    private String projectName;
    private String jdkVersion;
    private List<Dependency> dependencies;
    private List<ExposeInfo> exposedMethods;
    private List<TableInfo> tableInfos;
}
