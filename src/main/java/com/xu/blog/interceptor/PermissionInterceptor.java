package com.xu.blog.interceptor;

import com.xu.blog.annotation.RequirePermission;
import com.xu.blog.annotation.RequireRole;
import com.xu.blog.context.UserContext;
import com.xu.blog.param.UserToken;
import com.xu.blog.service.SysPermissionService;
import com.xu.blog.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 权限拦截器
 * 检查用户是否有访问接口的权限
 */
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysPermissionService sysPermissionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 如果不是Controller方法，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 检查角色权限
        RequireRole requireRole = handlerMethod.getMethodAnnotation(RequireRole.class);
        if (requireRole != null) {
            String requiredRole = requireRole.value();
            UserToken userToken = UserContext.getCurrentUser();

            if (userToken == null || userToken.getAccount() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
                return false;
            }

            String account = userToken.getAccount();
            String userRole = sysRoleService.getRoleCodeByAccount(account);

            // 权限等级: ADMIN > USER > GUEST
            if (!hasRequiredRole(userRole, requiredRole)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"权限不足，需要" + requiredRole + "角色\"}");
                return false;
            }
        }

        // 检查细粒度权限
        RequirePermission requirePermission = handlerMethod.getMethodAnnotation(RequirePermission.class);
        if (requirePermission != null) {
            String requiredPermission = requirePermission.value();
            UserToken userToken = UserContext.getCurrentUser();

            if (userToken == null || userToken.getAccount() == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":401,\"msg\":\"未登录\"}");
                return false;
            }

            String account = userToken.getAccount();
            boolean hasPermission = sysPermissionService.hasPermission(account, requiredPermission);

            if (!hasPermission) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"权限不足，需要" + requiredPermission + "权限\"}");
                return false;
            }
        }

        return true;
    }

    /**
     * 检查用户角色是否满足要求
     */
    private boolean hasRequiredRole(String userRole, String requiredRole) {
        // ADMIN 拥有所有权限
        if ("ADMIN".equals(userRole)) {
            return true;
        }

        // USER 可以访问 USER 和 GUEST 的接口
        if ("USER".equals(userRole)) {
            return "USER".equals(requiredRole) || "GUEST".equals(requiredRole);
        }

        // GUEST 只能访问 GUEST 的接口
        if ("GUEST".equals(userRole)) {
            return "GUEST".equals(requiredRole);
        }

        return false;
    }
}
