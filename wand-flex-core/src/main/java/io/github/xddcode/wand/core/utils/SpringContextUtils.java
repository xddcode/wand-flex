package io.github.xddcode.wand.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Map;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 11:37
 */
public class SpringContextUtils implements BeanFactoryAware {

    private static DefaultListableBeanFactory listableBeanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        listableBeanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public static <T> void registerSingleton(Class<T> type) {
        T obj = listableBeanFactory.createBean(type);
        String beanName = beanName(type.getName());
        listableBeanFactory.registerSingleton(beanName, obj);
    }

    public static void destroy(String className) {
        String beanName = beanName(className);
        listableBeanFactory.destroySingleton(beanName);
    }

    public static String beanName(String className) {
        String[] path = className.split("\\.");
        String beanName = path[path.length - 1];
        return Character.toLowerCase(beanName.charAt(0)) + beanName.substring(1);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) {
        return (T) listableBeanFactory.getBean(name);
    }

    public static <T> T getBean(Class<T> type) {
        return listableBeanFactory.getBean(type);
    }

    public static <T> Map<String, T> getBeans(Class<T> type) {
        return listableBeanFactory.getBeansOfType(type);
    }
}
