package com.llb.interception;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截的页面和请求映射
 * @Author llb
 * Date on 2020/3/25
 */
@Configuration
public class MvnConfig implements WebMvcConfigurer{

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {

            /**
             * 拦截除了login.html和/ 之外的所有请求
             * @param registry
             */
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                InterceptorRegistration interceptorRegistration = registry.addInterceptor(new LoginIntercept());

                interceptorRegistration.addPathPatterns("/**");
//                interceptorRegistration.excludePathPatterns("/", "/login.html", "/login", "/login/**", "/css/**", "/js/**", "/lib/**", "/images/**", "/fonts/**", "/error");
                interceptorRegistration.excludePathPatterns("/", "/**/login.html", "/**/login", "/**/login/**", "/**/css/**", "/**/js/**", "/**/lib/**", "/**/images/**", "/**/fonts/**", "/**/error");
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/login.html").setViewName("login");
            }
        };
    }
}
