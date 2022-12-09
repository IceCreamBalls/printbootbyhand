package com.ice.service;

import com.ice.spring.Autowired;
import com.ice.spring.Component;
import com.ice.spring.InitializingBean;
import com.ice.spring.Scope;
import org.springframework.beans.factory.BeanNameAware;

/**
 * @author liuhao
 * @date 2022年12月08日 1:41 下午
 */
@Component("userService")
@Scope("singleton")
public class UserService implements BeanNameAware, InitializingBean {

        @Autowired
        private OrderService orderService;

        private String beanName;

        public void test(){
            System.out.println(orderService);
            System.out.println(beanName);
        }

    @Override
    public void setBeanName(String s) {
        this.beanName = s;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化");
    }
}
