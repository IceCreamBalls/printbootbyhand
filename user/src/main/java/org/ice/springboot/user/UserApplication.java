package org.ice.springboot.user;


import com.ice.springboot.MySpringApplication;
import com.ice.springboot.MySpringBootApplication;
import com.ice.springboot.WebServerAutoConfiguration;
import org.springframework.context.annotation.Import;

@MySpringBootApplication
public class UserApplication {

    public static void main(String[] args) {
        MySpringApplication.run(UserApplication.class);
    }

}
