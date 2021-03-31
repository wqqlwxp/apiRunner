package com.runner.server.service.config;

import com.runner.server.service.auth.AuthFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //注册TestInterceptor拦截器
        InterceptorRegistration registration = registry.addInterceptor(new AuthFilter());
        registration.addPathPatterns("/**");//所有路径都被拦截
        registration.excludePathPatterns(
                "/auth/login" , //登录不拦截
                "/auth/registerUser"//   注册不拦截
        );
    }

}
