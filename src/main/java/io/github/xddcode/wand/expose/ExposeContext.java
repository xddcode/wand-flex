package io.github.xddcode.wand.expose;

import io.github.xddcode.wand.datasource.DatasourceManager;
import io.github.xddcode.wand.datasource.TableInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;

import java.util.List;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/14 16:05
 */
@Data
@Slf4j
public class ExposeContext {

    private String groupId;
    private String artifactId;
    private String version;
    private String projectName;
    private String jdkVersion;
    private List<Dependency> dependencies;
    private List<ExposeInfo> exposedMethods;
    private List<TableInfo> tableInfos;

    public static ExposeContext init() {
        ExposeContext exposeContext = new ExposeContext();
        exposeContext.setGroupId(DependencySupport.getGroupId());
        exposeContext.setArtifactId(DependencySupport.getArtifactId());
        exposeContext.setVersion(DependencySupport.getVersion());
        exposeContext.setProjectName(DependencySupport.getName());
        exposeContext.setJdkVersion(System.getProperty("java.version"));
        exposeContext.setDependencies(DependencySupport.getDependencies());
        exposeContext.setExposedMethods(ExposeMethodSupport.getExposeInfos());
        exposeContext.setTableInfos(DatasourceManager.getTableInfos());
        return exposeContext;
    }
}
