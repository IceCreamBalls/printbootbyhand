package com.ice.lh;

import com.ice.spring.MyApplicationContext;

/**
 * @author liuhao
 * @date 2022年12月08日 1:37 下午
 */
public class Test {
    public static void main(String[] args) {
        MyApplicationContext context = new MyApplicationContext(AppConfig.class);
        Object userService = context.getBean("userService");
        Object userService2 = context.getBean("userService");
        Object userService3 = context.getBean("userService");
        System.out.println(userService);
        System.out.println(userService2);
        System.out.println(userService3);

    }
}
