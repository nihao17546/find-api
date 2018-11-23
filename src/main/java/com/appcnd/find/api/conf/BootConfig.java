package com.appcnd.find.api.conf;

import com.appcnd.find.api.filter.AuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nihao 2018/11/23
 */
@Configuration
public class BootConfig {

    @Autowired
    private ProgramConfig programConfig;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new AuthFilter());
        registration.addUrlPatterns(programConfig.getFilterUris());
        registration.setName("authFilter");
        registration.setOrder(1);
        return registration;
    }

}
