package com.xu.blog.config;

import com.alibaba.fastjson2.JSON;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xu.common.context.UserContext;
import com.xu.common.enums.TokenEnum;
import com.xu.common.param.UserToken;
import com.xu.common.utils.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

@Slf4j
@Configuration
public class TokenHandlerAdapter implements HandlerInterceptor {

    /**
     * token分割符
     */
    private final static String TOKEN_SPLIT = "##";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 预检请求直接放行，不验证 token
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            // 从 http 请求头中取出 token
            String token = request.getHeader(TokenEnum.HEADER_TOKEN_KEY.getCode());
            if (StringUtils.isBlank(token)) {
                throw new RuntimeException(TokenEnum.IS_TOKEN_NULL.getMessage());
            }

            /**
             * 每个请求最多只有2分钟有效期
             */
            String[] str = token.split(TOKEN_SPLIT);
            //获取最近10分钟的时间戳
            Long time = System.currentTimeMillis();
            int time5int = (int) ((time - (time % (1000 * 60 * 1))) / 1000);
            Long time5 = time5int * 1000L  ;
            Long time10 = time5 - 1000 * 60 * 1;

            String md51 = DigestUtils.md5DigestAsHex((TokenEnum.KEY.getCode() + time5).getBytes()).toUpperCase();
            if (!str[1].equals(md51)) {
                String md52 = DigestUtils.md5DigestAsHex((TokenEnum.KEY.getCode() + time10).getBytes()).toUpperCase();
                if (!str[1].equals(md52)) {
                    throw new RuntimeException(TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
                }
            }

            /**
             * 解析token并放入session
             * @CurrentUser 注解获取用户信息
             */
            Map<String, Claim> map = JWTUtil.analysisToken(str[0]);
//            String username = map.get("userName").asString();
//            if (StringUtils.isBlank(username)) {
//                throw new RuntimeException(TokenEnum.ERROR_TOKEN_SIGN.getMessage());
//            }
            if (map.containsKey("user")) {
                String tokenStr = map.get("user").asString();
                UserToken userToken = JSON.parseObject(tokenStr, UserToken.class);

                // 将用户信息存储到ThreadLocal中，供全局使用
                UserContext.setCurrentUser(userToken);

                // 同时保留session存储，兼容旧代码
                request.getSession().setAttribute("Token", userToken);
                request.setAttribute("currentAccount", userToken != null ? userToken.getAccount() : null);

                return true;
            }
        } catch (TokenExpiredException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_OVERDUE.getMessage());
        } catch (JWTDecodeException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
        } catch (SignatureVerificationException e) {
            throw new RuntimeException(TokenEnum.ERROR_TOKEN_SIGN.getMessage());
        }
        return false;
    }

    /**
     * 请求完成后清理ThreadLocal，避免内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}
