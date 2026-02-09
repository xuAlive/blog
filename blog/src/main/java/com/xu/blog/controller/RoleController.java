package com.xu.blog.controller;

import com.xu.common.annotation.RequireRole;
import com.xu.blog.domain.SysRole;
import com.xu.blog.service.SysRoleService;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色管理Controller
 */
@RequestMapping("/blog/role")
@RestController
public class RoleController {

    private final SysRoleService sysRoleService;

    public RoleController(SysRoleService sysRoleService) {
        this.sysRoleService = sysRoleService;
    }

    /**
     * 获取当前用户的角色列表
     */
    @GetMapping("/getUserRoles")
    public Response getUserRoles() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }
        List<SysRole> roles = sysRoleService.getRolesByAccount(account);
        return Response.success(roles);
    }

    /**
     * 获取当前用户的角色编码
     */
    @GetMapping("/getUserRoleCode")
    public Response getUserRoleCode() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }
        String roleCode = sysRoleService.getRoleCodeByAccount(account);
        return Response.success(roleCode);
    }

    /**
     * 获取所有角色列表（管理员用）
     */
    @GetMapping("/getAllRoles")
    @RequireRole("ADMIN")
    public Response getAllRoles() {
        List<SysRole> roles = sysRoleService.list();
        return Response.success(roles);
    }

    /**
     * 为用户分配角色
     */
    @PostMapping("/assignToUser")
    @RequireRole("ADMIN")
    public Response assignRoleToUser(@RequestParam("account") String account,
                                      @RequestParam("roleId") Integer roleId) {
        boolean result = sysRoleService.assignRoleToUser(account, roleId);
        return result ? Response.success() : Response.error("分配角色失败");
    }

    /**
     * 新增角色
     */
    @PostMapping("/add")
    @RequireRole("ADMIN")
    public Response addRole(@RequestBody SysRole role) {
        boolean result = sysRoleService.save(role);
        return result ? Response.success() : Response.error("新增角色失败");
    }

    /**
     * 修改角色
     */
    @PostMapping("/update")
    @RequireRole("ADMIN")
    public Response updateRole(@RequestBody SysRole role) {
        boolean result = sysRoleService.updateById(role);
        return result ? Response.success() : Response.error("修改角色失败");
    }
}
