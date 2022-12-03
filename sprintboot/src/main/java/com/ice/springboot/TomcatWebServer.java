package com.ice.springboot;

/**
 * @author liuhao
 * @date 2022年12月03日 9:54 下午
 */
public class TomcatWebServer implements WebServer{
    @Override
    public void start() {
        System.out.println("启动Tomcat");
    }
}
