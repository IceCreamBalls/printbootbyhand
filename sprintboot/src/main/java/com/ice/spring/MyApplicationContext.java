package com.ice.spring;

import org.springframework.beans.factory.BeanNameAware;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author liuhao
 * @date 2022年12月08日 1:36 下午
 */
public class MyApplicationContext {
    private Class configClass;

    private ConcurrentHashMap<String, Object> singletonObject = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<BeanPostProcessor> beanPostProcessorList  = new ArrayList<>();



    public MyApplicationContext(Class configClass) {
        this.configClass = configClass;
        // ComponentScan 注解->扫描路径->扫描->BeanDefinition->BeanDefinitionMap
        scan(configClass);

        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")){
                Object bean = createBean(beanName,beanDefinition);
                singletonObject.put(beanName, bean);
            }
        }


    }

    private Object createBean(String beanName,BeanDefinition beanDefinition) {
        Class clazz = beanDefinition.getClazz();
        try {
            Object o = clazz.getDeclaredConstructor().newInstance();

            //依赖注入
            for (Field declaredField : clazz.getDeclaredFields()) {
                if (declaredField.isAnnotationPresent(Autowired.class)){

                    Object bean = getBean(declaredField.getName());
                    declaredField.setAccessible(true);
                    declaredField.set(o, bean);
                }
            }
            //Aware 回调
            if(o instanceof BeanNameAware){
                ((BeanNameAware) o).setBeanName(beanName);
            }

            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessBeforeInitialization(beanName, o);
            }

            //初始化
            if(o instanceof InitializingBean ){
                ((InitializingBean)o).afterPropertiesSet();
            }

            //BeanPostProcessor
            for (BeanPostProcessor beanPostProcessor : beanPostProcessorList) {
                beanPostProcessor.postProcessAfterInitialization(beanName, o);
            }
            return o;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void scan(Class configClass) {
        //1.解析配置类

        ComponentScan componentScan = (ComponentScan) configClass.getDeclaredAnnotation(ComponentScan.class);
        String path = componentScan.value();

        // 扫描
        // BootStrap   jre/lib
        // Ext     jre/ext/lib
        // App     classpath
        ClassLoader classLoader = MyApplicationContext.class.getClassLoader();
        System.out.println(path);
        path =  path.replace(".","/");
        URL resource = classLoader.getResource(path);
        File file = new File(resource.getFile());
        if(file.isDirectory()){
            File[] files = file.listFiles();
            for (File file1 : files) {
                String absolutePath = file1.getAbsolutePath();
                System.out.println(absolutePath);
                String className = absolutePath.substring(absolutePath.indexOf("com"), absolutePath.indexOf(".class"));
                className =  className.replace("/", ".");

                try {
                    Class<?> clazz = classLoader.loadClass(className);
                    if(clazz.isAnnotationPresent(Component.class)){
                        //表示这是一个bean
                            //解析类->BeanDefinition，判断当前bean是单例bean，还是prototype的bean
                        Component component = clazz.getDeclaredAnnotation(Component.class);
                        String beanName = component.value();

                        if(BeanPostProcessor.class.isAssignableFrom(clazz)){
                            try {
                                BeanPostProcessor beanPostProcessor = (BeanPostProcessor) clazz.getDeclaredConstructor().newInstance();
                                beanPostProcessorList.add(beanPostProcessor);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                e.printStackTrace();
                            } catch (NoSuchMethodException e) {
                                e.printStackTrace();
                            }

                        }

                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setClazz(clazz);
                        if(clazz.isAnnotationPresent(Scope.class)){
                            Scope scopeAnnotation = clazz.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        }else{
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitionMap.put(beanName, beanDefinition);
                        //生成一个bean对象

                    }

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    public Object getBean(String beanName){
        if(beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if(beanDefinition.getScope().equals("singleton")){
                Object o = singletonObject.get(beanName);
                return o;
            }else {
                Object bean = createBean(beanName,beanDefinition);
                return bean;
            }

        }else {
            throw  new NullPointerException("不存在对应的bean");
        }

    }
}
