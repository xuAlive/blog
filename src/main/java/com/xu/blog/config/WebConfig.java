package com.xu.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 添加CORS映射，允许来自特定域的请求访问
        registry.addMapping("/**")
                .allowedOrigins("http://127.0.0.1:5174") // 允许来自特定域的请求（根据你的前端部署地址进行修改）
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE") // 允许的请求方法（根据需要添加其他方法）
                .allowedHeaders("*") // 允许的请求头部（根据需要添加其他头部）
                .allowCredentials(true); // 允许携带凭据
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //token 拦截
        registry.addInterceptor(new TokenHandlerAdapter())
                .addPathPatterns("/blog/**")
                //登陆接口不验证token
                .excludePathPatterns("/blog/sys/**");
    }
}