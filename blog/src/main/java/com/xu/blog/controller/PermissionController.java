package com.xu.blog.controller;

import com.xu.common.annotation.RequireRole;
import com.xu.blog.domain.SysPermission;
import com.xu.blog.service.SysPermissionService;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 权限管理Controller
 */
@RequestMapping("/blog/permission")
@RestController
public class PermissionController {

    private final SysPermissionService sysPermissionService;

    public PermissionController(SysPermissionService sysPermissionService) {
        this.sysPermissionService = sysPermissionService;
    }

    /**
     * 获取当前用户的权限列表
     */
    @GetMapping("/getUserPermissions")
    public Response getUserPermissions() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }
        List<SysPermission> permissions = sysPermissionService.getPermissionsByAccount(account);
        return Response.success(permissions);
    }

    /**
     * 获取所有权限列表（管理员用）
     */
    @GetMapping("/getAllPermissions")
    @RequireRole("ADMIN")
    public Response getAllPermissions() {
        List<SysPermission> permissions = sysPermissionService.getAllValidPermissions();
        return Response.success(permissions);
    }

    /**
     * 新增权限
     */
    @PostMapping("/add")
    @RequireRole("ADMIN")
    public Response addPermission(@RequestBody SysPermission permission) {
        boolean result = sysPermissionService.save(permission);
        return result ? Response.success() : Response.error("新增权限失败");
    }

    /**
     * 修改权限
     */
    @PostMapping("/update")
    @RequireRole("ADMIN")
    public Response updatePermission(@RequestBody SysPermission permission) {
        boolean result = sysPermissionService.updateById(permission);
        return result ? Response.success() : Response.error("修改权限失败");
    }

    /**
     * 为角色分配权限
     */
    @PostMapping("/assignToRole")
    @RequireRole("ADMIN")
    public Response assignPermissionsToRole(@RequestParam("roleId") Integer roleId,
                                             @RequestBody List<Integer> permissionIds) {
        boolean result = sysPermissionService.assignPermissionsToRole(roleId, permissionIds);
        return result ? Response.success() : Response.error("分配权限失败");
    }
}
