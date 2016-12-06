/*

This file is part of iTest.

Copyright (C) 2016
   Marcos Martinez Cañete(mmartinezcan@alumnos.urjc.es)
   Jose Manuel Colmenar Verdugo (josemanuel.colmenar@urjc.es)

iTest is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

iTest is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.
You should have received a copy of the GNU General Public License
along with iTest.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.itest.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.http.CacheControl;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@EnableWebMvc
@Configuration
@PropertySource("classpath:application.properties")
public class WebConfig extends WebMvcConfigurerAdapter {

    // Constants of properties to config the message source
    private static final String MESSAGE_SOURCE_BASENAME = "spring.messages.basename";
    private static final String MESSAGE_SOURCE_ENCODING = "spring.messages.encoding";

    // Constants of properties to config the data source
    private static final String DATA_SOURCE_DRIVER = "spring.datasource.driver-class-name";
    private static final String DATA_SOURCE_URL = "spring.datasource.url";
    private static final String DATA_SOURCE_USERNAME = "spring.datasource.username";
    private static final String DATA_SOURCE_PASSWORD = "spring.datasource.password";

    // Constants of properties to config the thymeleaf template
    private static final String THYMELEAF_CACHE = "spring.thymeleaf.cache";
    private static final String THYMELEAF_PREFIX = "spring.thymeleaf.prefix";
    private static final String THYMELEAF_SUFFIX = "spring.thymeleaf.suffix";
    private static final String THYMELEAF_ENCODING = "spring.thymeleaf.encoding";
    private static final String THYMELEAF_MODE = "spring.thymeleaf.mode";

    @Autowired
    private Environment environment;

    // The application context
    private ApplicationContext applicationContext;

    public WebConfig(ApplicationContext applicationContext){
        super();

        // Set the ApplicationContext
        this.applicationContext = applicationContext;
    }

    /// Default servlet handling configuration ///
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    /// DataSource configuration ///
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        driverManagerDataSource.setDriverClassName(this.environment.getRequiredProperty(DATA_SOURCE_DRIVER));
        driverManagerDataSource.setUrl(this.environment.getRequiredProperty(DATA_SOURCE_URL));
        driverManagerDataSource.setUsername(this.environment.getRequiredProperty(DATA_SOURCE_USERNAME));
        driverManagerDataSource.setPassword(this.environment.getRequiredProperty(DATA_SOURCE_PASSWORD));
        return driverManagerDataSource;
    }

    /// Resources configuration ///
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Set the cache control ("no-cstore") to static html pages. This is to avoid access to unauthorized web pages from cache
        registry.addResourceHandler("/admin/*").addResourceLocations("/admin/").setCacheControl(CacheControl.noStore());
        registry.addResourceHandler("/tutor/*").addResourceLocations("/tutor/").setCacheControl(CacheControl.noStore());
        registry.addResourceHandler("/learner/*").addResourceLocations("/learner/").setCacheControl(CacheControl.noStore());

        // Store the resources in cache
        registry.addResourceHandler("/resources/angularjs/*").addResourceLocations("/resources/angularjs/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/angularjs/lib/*").addResourceLocations("/resources/angularjs/lib/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/css/*").addResourceLocations("/resources/css/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/fonts/*").addResourceLocations("/resources/fonts/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/img/*").addResourceLocations("/resources/img/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));
        registry.addResourceHandler("/resources/js/*").addResourceLocations("/resources/js/").setCacheControl(CacheControl.maxAge(Long.MAX_VALUE, TimeUnit.DAYS));

        super.addResourceHandlers(registry);
    }

    /// Message source configuration ///
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename(this.environment.getRequiredProperty(MESSAGE_SOURCE_BASENAME));
        messageSource.setDefaultEncoding(this.environment.getRequiredProperty(MESSAGE_SOURCE_ENCODING));
        return messageSource;
    }

    /// Thymeleaf template configuration ///
    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding(THYMELEAF_ENCODING);
        return resolver;
    }

    @Bean
    public TemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setEnableSpringELCompiler(true);
        engine.setTemplateResolver(templateResolver());
        return engine;
    }

    private ITemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(this.applicationContext);
        resolver.setPrefix(this.environment.getRequiredProperty(THYMELEAF_PREFIX));
        resolver.setSuffix(this.environment.getRequiredProperty(THYMELEAF_SUFFIX));
        resolver.setCacheable(Boolean.parseBoolean(this.environment.getRequiredProperty(THYMELEAF_CACHE)));
        resolver.setTemplateMode(this.environment.getRequiredProperty(THYMELEAF_MODE));
        return resolver;
    }
}
