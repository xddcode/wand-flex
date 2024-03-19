package io.github.xddcode.wand.core.dynamic;

import io.github.xddcode.wand.core.exception.LoaderRuntimeException;
import io.github.xddcode.wand.core.utils.SpringContextUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;

/**
 * 动态Controller类加载
 *
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:37
 */
@Slf4j
@Builder
public class DynamicController {

    private DynamicClass dynamicClass;

    public DynamicController(DynamicClass dynamicClass) {
        this.dynamicClass = dynamicClass;
    }

    /**
     * 加载并注册动态类的Controller
     */
    public void load() {
        String beanName = SpringContextUtils.beanName(dynamicClass.getFullClassName());
        // 销毁Bean
        SpringContextUtils.destroy(beanName);
        // 每次都是new新的ClassLoader对象
        Class<?> type = dynamicClass.compiler().load();
        //先注册bean，否则卸载的时候会报错找不到bean
        SpringContextUtils.registerSingleton(type);
        Object object = SpringContextUtils.getBean(beanName);
        //先移除原有的映射，否则多次注册会报错
        unload(object.getClass());
        //注册新的映射
        load(type);
    }

    public void load(Class<?> type) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringContextUtils.getBean("requestMappingHandlerMapping");
        try {
            Method method = requestMappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod("detectHandlerMethods", Object.class);
            method.setAccessible(true);
            method.invoke(requestMappingHandlerMapping, type.getConstructor().newInstance());
        } catch (Exception e) {
            log.error("[DynamicController] load error: {}", e.getMessage());
            throw new LoaderRuntimeException("Failed to dynamically load controller due to reflection errors.");
        }
    }

    public void unload() {
        String beanName = SpringContextUtils.beanName(dynamicClass.getFullClassName());
        Object bean = SpringContextUtils.getBean(beanName);
        unload(bean.getClass());
        // 销毁Bean
        SpringContextUtils.destroy(beanName);
    }

    public void unload(Class<?> clazz) {
        RequestMappingHandlerMapping requestMappingHandlerMapping = SpringContextUtils.getBean("requestMappingHandlerMapping");
        ReflectionUtils.doWithMethods(clazz, method -> {
            Method specificMethod = ClassUtils.getMostSpecificMethod(method, clazz);
            try {
                Method createMappingMethod = RequestMappingHandlerMapping.class.getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                createMappingMethod.setAccessible(true);
                RequestMappingInfo requestMappingInfo = (RequestMappingInfo) createMappingMethod.invoke(requestMappingHandlerMapping, specificMethod, clazz);
                if (requestMappingInfo != null) {
                    requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                }
            } catch (Exception e) {
                throw new LoaderRuntimeException("Failed to unregister controller mappings due to reflection errors.");
            }
        }, ReflectionUtils.USER_DECLARED_METHODS);
    }
}
