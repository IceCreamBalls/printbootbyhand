package com.ice.service;

import com.ice.spring.Autowired;
import com.ice.spring.Component;
import com.ice.spring.Scope;

/**
 * @author liuhao
 * @date 2022年12月08日 1:41 下午
 */
@Component("userService")
@Scope("singleton")
public class UserService {

        @Autowired
        private OrderService orderService;

        public void test(){
            System.out.println(orderService);
        }
}
