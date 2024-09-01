package com.agnasarp.soapcoursemanagement.configuration;


import java.util.Collections;
import java.util.List;

import org.apache.wss4j.dom.WSConstants;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j2.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j2.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

//Spring configuration
@Configuration
//Enable Spring Web Service
@EnableWs
public class WebServiceConfiguration extends WsConfigurerAdapter {

    //    Create a MessageDispatcherServlet and map a URI
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {

        MessageDispatcherServlet messageDispatcherServlet = new MessageDispatcherServlet();
        messageDispatcherServlet.setApplicationContext(applicationContext);
        messageDispatcherServlet.setTransformWsdlLocations(true);

        return new ServletRegistrationBean(messageDispatcherServlet, "/ws/*");
    }

    //    Create XSD schema
    @Bean
    public XsdSchema coursesSchema() {

        return new SimpleXsdSchema(new ClassPathResource("course-details.xsd"));
    }

    //    Create a WSDL from the above schema
    @Bean(name = "courses")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema coursesSchema) {

        DefaultWsdl11Definition defaultWsdl11Definition = new DefaultWsdl11Definition();

        defaultWsdl11Definition.setPortTypeName("CoursePort");
        defaultWsdl11Definition.setTargetNamespace("https://www.agnasarp.com/courses");
        defaultWsdl11Definition.setLocationUri("/ws");
        defaultWsdl11Definition.setSchema(coursesSchema);

        return defaultWsdl11Definition;
    }

     @Bean
    public Wss4jSecurityInterceptor wss4jSecurityInterceptor() {
        Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
        interceptor.setValidationActions(WSConstants.USERNAME_TOKEN_LN);
        interceptor.setValidationCallbackHandler(callbackHandler());
        return interceptor;
    }

    @Bean
    public SimplePasswordValidationCallbackHandler callbackHandler() {
        SimplePasswordValidationCallbackHandler handler = new SimplePasswordValidationCallbackHandler();
        handler.setUsersMap(Collections.singletonMap("user", "password"));
        return handler;
    }

    //Interceptors.add -> XwsSecurityInterceptor
    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        interceptors.add(wss4jSecurityInterceptor());
    }
}
