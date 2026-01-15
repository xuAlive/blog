package com.xu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.blog.domain.SysPermission;

import java.util.List;

/**
 * 权限Service
 */
public interface SysPermissionService extends IService<SysPermission> {

    /**
     * 根据用户账号获取权限列表
     */
    List<SysPermission> getPermissionsByAccount(String account);

    /**
     * 检查用户是否有指定权限
     */
    boolean hasPermission(String account, String permissionCode);

    /**
     * 为角色分配权限
     */
    boolean assignPermissionsToRole(Integer roleId, List<Integer> permissionIds);

    /**
     * 获取所有有效权限列表
     */
    List<SysPermission> getAllValidPermissions();
}
