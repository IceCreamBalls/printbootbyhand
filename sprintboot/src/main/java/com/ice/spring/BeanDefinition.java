package com.ice.spring;

/**
 * @author liuhao
 * @date 2022年12月08日 2:26 下午
 */
public class BeanDefinition {
    private Class clazz;
    private String scope;


    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }
}
