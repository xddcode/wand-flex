package io.github.xddcode.wand.expose;

import io.github.xddcode.wand.exception.ExposeRuntimeException;
import io.github.xddcode.wand.expose.annotation.Expose;
import io.github.xddcode.wand.expose.annotation.ExposeMethod;
import io.github.xddcode.wand.expose.annotation.ExposeParameter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AbstractTypeHierarchyTraversingFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:40
 */
@Slf4j
public class ExposeMethodSupport {

    @Getter
    private static final List<ExposeInfo> exposeInfos = new ArrayList<>();

    public static void init(String basePackage) {
        log.info("Scanning base package: {}", basePackage);
        exposeInfos.clear();
        ClassPathScanningCandidateComponentProvider provider =
                new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AbstractTypeHierarchyTraversingFilter(true, true) { /* 留空 */
        });

        provider.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            try {
                Class<?> cls = Class.forName(beanDefinition.getBeanClassName());
                processClass(cls);
            } catch (ClassNotFoundException e) {
                log.error("Failed to load class: {}", beanDefinition.getBeanClassName(), e);
                throw new ExposeRuntimeException("Failed to load class '" + beanDefinition.getBeanClassName() + "'. " + e.getMessage(), e);
            }
        });
    }

    private static void processClass(Class<?> cls) {
        Expose exposeAnnotation = cls.getAnnotation(Expose.class);
        if (exposeAnnotation == null) return;
        ExposeInfo exposeInfo = new ExposeInfo();
        exposeInfo.setName(cls.getSimpleName());
        exposeInfo.setDescription(exposeAnnotation.description());
        exposeInfo.setType(exposeAnnotation.type());
        List<ExposeInfo.Method> methods = Arrays.stream(cls.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(ExposeMethod.class))
                .map(ExposeMethodSupport::mapMethod)
                .collect(Collectors.toList());
        if (!methods.isEmpty()) {
            exposeInfo.setMethods(methods);
            exposeInfos.add(exposeInfo);
        }
    }

    private static ExposeInfo.Method mapMethod(Method method) {
        ExposeMethod annotation = method.getAnnotation(ExposeMethod.class);
        ExposeInfo.Method exposeMethod = new ExposeInfo.Method();
        exposeMethod.setName(method.getName());
        exposeMethod.setDescription(annotation.description());
        List<ExposeInfo.Parameter> parameters = Arrays.stream(annotation.parameters())
                .map(ExposeMethodSupport::mapParameter)
                .collect(Collectors.toList());
        exposeMethod.setParameters(parameters);
        return exposeMethod;
    }

    private static ExposeInfo.Parameter mapParameter(ExposeParameter exposeParameter) {
        ExposeInfo.Parameter parameter = new ExposeInfo.Parameter();
        parameter.setName(exposeParameter.name());
        parameter.setDataType(exposeParameter.dataType().getType());
        parameter.setDescription(exposeParameter.description());
        return parameter;
    }
}
