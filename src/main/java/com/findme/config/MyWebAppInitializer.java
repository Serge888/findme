package com.findme.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class MyWebAppInitializer implements WebApplicationInitializer {


    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext stx = new AnnotationConfigWebApplicationContext();
        stx.register(AppConfig.class);

        servletContext.addListener(new ContextLoaderListener(stx));

        stx.setServletContext(servletContext);

        ServletRegistration.Dynamic servlet = servletContext.addServlet(
                "dispatcher", new DispatcherServlet(stx));

        servlet.addMapping("/");
        servlet.setLoadOnStartup(1);
    }
}
