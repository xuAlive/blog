package com.xu.schedule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 配置 CORS 和拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenHandlerAdapter tokenHandlerAdapter;

    public WebConfig(TokenHandlerAdapter tokenHandlerAdapter) {
        this.tokenHandlerAdapter = tokenHandlerAdapter;
    }

    /**
     * CORS 跨域配置
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Token 验证拦截器
        registry.addInterceptor(tokenHandlerAdapter)
                .addPathPatterns("/schedule/**")
                .excludePathPatterns(
                        "/schedule/health"  // 健康检查接口不需要验证
                );
    }
}
