package com.xu.blog.config;

import com.xu.blog.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final PermissionInterceptor permissionInterceptor;

    public WebConfig(PermissionInterceptor permissionInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //token 拦截 - 第一层拦截，验证token并设置用户信息
        registry.addInterceptor(new TokenHandlerAdapter())
                .addPathPatterns("/blog/**")
                //只排除登录和注册接口，其他接口都需要验证token
                .excludePathPatterns("/blog/sys/login", "/blog/sys/register");

        //权限拦截 - 第二层拦截，验证用户权限
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/blog/**")
                //只排除登录和注册接口，其他接口都需要验证权限
                .excludePathPatterns("/blog/sys/login", "/blog/sys/register");
    }
}
