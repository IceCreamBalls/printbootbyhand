package com.ice.springboot;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

/**
 * @author liuhao
 * @date 2022年12月03日 10:09 下午
 */
public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        try {
            Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(MyCondition.class.getName());

            String value = (String) annotationAttributes.get("value");
            context.getClassLoader().loadClass(value);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
