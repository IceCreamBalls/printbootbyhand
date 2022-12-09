package com.ice.service;

import com.ice.spring.BeanPostProcessor;
import com.ice.spring.Component;

/**
 * @author liuhao
 * @date 2022年12月08日 7:29 下午
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public void postProcessBeforeInitialization(String beanName, Object bean) {
        System.out.println("初始化前");

    }

    @Override
    public void postProcessAfterInitialization(String beanName, Object bean) {
        System.out.println("初始化后");
    }
}
