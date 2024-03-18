package io.github.xddcode.wand.core;

import io.github.xddcode.wand.utils.SpringContextUtils;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author: hao.ding@insentek.com
 * @Date: 2024/2/20 14:09
 */
@Slf4j
@Builder
public class DynamicBean {

    private DynamicClass dynamicClass;

    public DynamicBean(DynamicClass dynamicClass) {
        this.dynamicClass = dynamicClass;
    }

    /**
     * 加载动态生成的类并将其注册为单例Bean，
     * 销毁同名的Bean对象，返回新加载类的Bean名称
     *
     * @return 新加载类的Bean名称
     */
    public String load() {
        String beanName = SpringContextUtils.beanName(dynamicClass.getFullClassName());
        // 销毁Bean
        SpringContextUtils.destroy(beanName);
        // 每次都是new新的ClassLoader对象
        Class<?> type = dynamicClass.compiler().load();
        SpringContextUtils.registerSingleton(type);
        return beanName;
    }
}
