package com.ice.spring;

/**
 * @author liuhao
 * @date 2022年12月08日 7:28 下午
 */
public interface BeanPostProcessor {
    public void postProcessBeforeInitialization(String beanName, Object bean);
    public void postProcessAfterInitialization(String beanName, Object bean);
}
