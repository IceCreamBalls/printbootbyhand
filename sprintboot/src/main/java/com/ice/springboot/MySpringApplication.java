package com.ice.springboot;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.ApplicationContext;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.util.Map;

/**
 * @author liuhao
 * @date 2022年12月03日 6:15 下午
 */
public class MySpringApplication {
    public static void run(Class clazz) {
        //创建一个spring容器
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(clazz);
        context.refresh();

        //启动Tomcat
//        startTomcat(context);
        WebServer webServer = getWebServer(context);
        webServer.start();
    }

    private static WebServer getWebServer(WebApplicationContext applicationContext) {
        //判断返回TomcatWebServer 还是JettyWebServer
        Map<String, WebServer> beansOfType = applicationContext.getBeansOfType(WebServer.class);

        if(beansOfType.size() ==  0){
            throw new NullPointerException();
        }

        if(beansOfType.size() > 1){
            throw  new IllegalStateException();
        }

        return  beansOfType.values().stream().findFirst().get();
    }

    private static void startTomcat(WebApplicationContext applicatinContext) {
        Tomcat tomcat = new Tomcat();
        Server server = tomcat.getServer();
        Service service = server.findService("Tomcat");

        Connector connector = new Connector();
        connector.setPort(8081);

        StandardEngine engine = new StandardEngine();
        engine.setDefaultHost("localhost");

        StandardHost host = new StandardHost();
        host.setName("localhost");

        String contextPath="";
        StandardContext context = new StandardContext();
        context.setPath(contextPath);
        context.addLifecycleListener(new Tomcat.FixContextListener());

        host.addChild(context);
        engine.addChild(host);
        service.setContainer(engine);
        service.addConnector(connector);

        tomcat.addServlet(contextPath, "dispatcher", new DispatcherServlet(applicatinContext));
        context.addServletMappingDecoded("/*", "dispatcher");

        try{
            tomcat.start();
        }catch (LifecycleException e){
            e.printStackTrace();
        }
    }
}
