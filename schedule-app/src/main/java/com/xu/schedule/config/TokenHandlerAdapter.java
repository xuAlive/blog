package com.xu.schedule.config;

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
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Token 验证拦截器
 * 验证请求中的 JWT Token，设置用户上下文
 */
@Slf4j
@Component
public class TokenHandlerAdapter implements HandlerInterceptor {

    private static final String TOKEN_SPLIT = "##";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // OPTIONS 预检请求直接放行
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        try {
            // 从请求头获取 token
            String token = request.getHeader(TokenEnum.HEADER_TOKEN_KEY.getCode());
            if (StringUtils.isBlank(token)) {
                sendError(response, 401, "未登录，请先登录");
                return false;
            }

            // 验证 token 时间戳（每个请求最多2分钟有效期）
            String[] str = token.split(TOKEN_SPLIT);
            if (str.length != 2) {
                sendError(response, 401, "Token格式错误");
                return false;
            }

            Long time = System.currentTimeMillis();
            int time5int = (int) ((time - (time % (1000 * 60 * 1))) / 1000);
            Long time5 = time5int * 1000L;
            Long time10 = time5 - 1000 * 60 * 1;

            String md51 = DigestUtils.md5DigestAsHex((TokenEnum.KEY.getCode() + time5).getBytes()).toUpperCase();
            if (!str[1].equals(md51)) {
                String md52 = DigestUtils.md5DigestAsHex((TokenEnum.KEY.getCode() + time10).getBytes()).toUpperCase();
                if (!str[1].equals(md52)) {
                    sendError(response, 401, "Token已过期，请重新登录");
                    return false;
                }
            }

            // 解析 JWT
            Map<String, Claim> map = JWTUtil.analysisToken(str[0]);
            if (map.containsKey("user")) {
                String tokenStr = map.get("user").asString();
                UserToken userToken = JSON.parseObject(tokenStr, UserToken.class);

                // 存储到 ThreadLocal
                UserContext.setCurrentUser(userToken);

                // 兼容 session
                request.getSession().setAttribute("Token", userToken);
                request.setAttribute("currentAccount", userToken != null ? userToken.getAccount() : null);

                return true;
            }

            sendError(response, 401, "Token解析失败");
            return false;

        } catch (TokenExpiredException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_OVERDUE.getMessage());
            return false;
        } catch (JWTDecodeException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_ANALYSIS.getMessage());
            return false;
        } catch (SignatureVerificationException e) {
            sendError(response, 401, TokenEnum.ERROR_TOKEN_SIGN.getMessage());
            return false;
        } catch (Exception e) {
            log.error("Token验证异常", e);
            sendError(response, 401, "Token验证失败");
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }

    private void sendError(HttpServletResponse response, int status, String message) {
        try {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":" + status + ",\"codeMessage\":\"" + message + "\"}");
        } catch (Exception e) {
            log.error("发送错误响应失败", e);
        }
    }
}
