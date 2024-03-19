package io.github.xddcode.wand.core.expose;


import io.github.xddcode.wand.core.datasource.DatasourceManager;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/14 16:05
 */
public class ExposeContext {

    private DatasourceManager datasourceManager;

    public ExposeContext(DatasourceManager datasourceManager) {
        this.datasourceManager = datasourceManager;
    }

    public ExposeResource init() {
        ExposeResource exposeResource = new ExposeResource();
        exposeResource.setGroupId(DependencySupport.getGroupId());
        exposeResource.setArtifactId(DependencySupport.getArtifactId());
        exposeResource.setVersion(DependencySupport.getVersion());
        exposeResource.setProjectName(DependencySupport.getName());
        exposeResource.setJdkVersion(System.getProperty("java.version"));
        exposeResource.setDependencies(DependencySupport.getDependencies());
        exposeResource.setExposedMethods(ExposeMethodSupport.getExposeInfos());
        exposeResource.setTableInfos(datasourceManager.getTableInfos());
        return exposeResource;
    }
}
