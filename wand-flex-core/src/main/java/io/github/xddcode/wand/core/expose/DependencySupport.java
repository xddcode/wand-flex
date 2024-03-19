package io.github.xddcode.wand.core.expose;

import io.github.xddcode.wand.core.exception.ExposeRuntimeException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * maven依赖项支持
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/6 13:59
 */
@Slf4j
public class DependencySupport {

    private static final String pomPath = System.getProperty("user.dir") + "/pom.xml";
    @Getter
    private static List<Dependency> dependencies = new ArrayList<>();
    @Getter
    private static String groupId;
    @Getter
    private static String artifactId;
    @Getter
    private static String version;
    @Getter
    private static String name;

    public static void init() {
        try (FileReader reader = new FileReader(pomPath)) {
            dependencies.clear();
            MavenXpp3Reader xpp3Reader = new MavenXpp3Reader();
            Model model = xpp3Reader.read(reader);
            // 添加新的依赖项
            dependencies.addAll(model.getDependencies());
            groupId = model.getGroupId();
            artifactId = model.getArtifactId();
            version = model.getVersion();
            name = model.getName();
        } catch (Exception e) {
            throw new ExposeRuntimeException("Failed to initialize dependencies from pom.xml. Please check if the file exists, is accessible, and is correctly formatted.");
        }
    }
}
