package org.ice.springboot.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liuhao
 * @date 2022年12月03日 9:17 下午
 */
@RestController
public class UserController {

    @GetMapping("/test")
    public String test(){
        return "123";
    }
}
