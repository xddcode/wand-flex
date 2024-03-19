package io.github.xddcode.wand.core.expose;

import io.github.xddcode.wand.core.expose.annotation.Expose;
import io.github.xddcode.wand.core.expose.annotation.ExposeMethod;
import io.github.xddcode.wand.core.expose.annotation.ExposeParameter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.reflections.scanners.Scanners.SubTypes;
import static org.reflections.scanners.Scanners.TypesAnnotated;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/3/5 13:40
 */
@Slf4j
public class ExposeMethodSupport {

    @Getter
    private static final List<ExposeInfo> exposeInfos = new ArrayList<>();

    public static void init(String scanPackage) {
        log.info("Scanning base package: {}", scanPackage);
        exposeInfos.clear();
        Reflections reflections = new Reflections(scanPackage);
        Set<Class<?>> classes =
                reflections.get(SubTypes.of(TypesAnnotated.with(Expose.class)).asClass());
        for (Class<?> cls : classes) {
            processClass(cls);
        }
    }

    private static void processClass(Class<?> cls) {
        Expose exposeAnnotation = cls.getAnnotation(Expose.class);
        if (exposeAnnotation == null) return;
        ExposeInfo exposeInfo = new ExposeInfo();
        exposeInfo.setPackageName(cls.getPackage().getName());
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
