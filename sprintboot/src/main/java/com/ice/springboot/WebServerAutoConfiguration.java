package com.ice.springboot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author liuhao
 * @date 2022年12月03日 10:06 下午
 */
@Configuration
public class WebServerAutoConfiguration {

    @Bean
    @Conditional(TomcatCondition.class)
    public TomcatWebServer tomcatWebServer(){
        return new TomcatWebServer();
    }

    @Bean
    @Conditional(JettyCondition.class)
    public JettyWebServer jettyWebServer(){
        return new JettyWebServer();
    }
}
