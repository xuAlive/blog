package com.xu.calendar.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web 配置类
 * 配置拦截器
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenHandlerAdapter tokenHandlerAdapter;

    public WebConfig(TokenHandlerAdapter tokenHandlerAdapter) {
        this.tokenHandlerAdapter = tokenHandlerAdapter;
    }

    /**
     * 拦截器配置
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // Token 验证拦截器
        registry.addInterceptor(tokenHandlerAdapter)
                .addPathPatterns("/calendar/**")
                .excludePathPatterns(
                        "/calendar/health"  // 健康检查接口不需要验证
                );
    }
}
