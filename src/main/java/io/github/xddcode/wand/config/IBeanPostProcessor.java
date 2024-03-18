//package com.insentek.wand.config;
//
//import com.insentek.wand.expose.ExposeMethodSupport;
//import com.insentek.wand.expose.annotation.EnableExposeMethod;
//import com.insentek.wand.expose.DependencySupport;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.BeansException;
//import org.springframework.beans.factory.config.BeanPostProcessor;
//import org.springframework.core.annotation.AnnotationUtils;
//
//import java.util.concurrent.atomic.AtomicBoolean;
//
///**
// * @Author: hao.ding@insentek.com
// * @Date: 2024/3/5 13:40
// */
//public class IBeanPostProcessor implements BeanPostProcessor {
//
//    private static final AtomicBoolean initialized = new AtomicBoolean(false);
//
//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (initialized.compareAndSet(false, true)) {}
//        EnableExposeMethod annotation = AnnotationUtils.findAnnotation(bean.getClass(), EnableExposeMethod.class);
//        if (annotation != null) {
//            String basePackage = bean.getClass().getPackage().getName();
//            if (StringUtils.isNotEmpty(annotation.basePackage())) {
//                basePackage = annotation.basePackage();
//            }
//            //初始化扫描词包内所有携带特定注解的方法
//            ExposeMethodSupport.init(basePackage);
//        }
//        //初始化扫描pom依赖列表
//        DependencySupport.init();
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        return bean;
//    }
//}
