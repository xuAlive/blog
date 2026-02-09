package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.SysPermission;
import com.xu.blog.domain.SysRolePermission;
import com.xu.blog.mapper.SysPermissionMapper;
import com.xu.blog.mapper.SysRolePermissionMapper;
import com.xu.blog.service.SysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 权限Service实现
 */
@Slf4j
@Service
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements SysPermissionService {

    private final SysRolePermissionMapper sysRolePermissionMapper;

    public SysPermissionServiceImpl(SysRolePermissionMapper sysRolePermissionMapper) {
        this.sysRolePermissionMapper = sysRolePermissionMapper;
    }

    @Override
    public List<SysPermission> getPermissionsByAccount(String account) {
        return baseMapper.selectPermissionsByAccount(account);
    }

    @Override
    public boolean hasPermission(String account, String permissionCode) {
        List<SysPermission> permissions = getPermissionsByAccount(account);
        return permissions.stream()
                .anyMatch(p -> p.getPermissionCode().equals(permissionCode));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignPermissionsToRole(Integer roleId, List<Integer> permissionIds) {
        try {
            // 先删除原有的权限分配
            QueryWrapper<SysRolePermission> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("role_id", roleId);
            sysRolePermissionMapper.delete(deleteWrapper);

            // 分配新权限
            if (permissionIds != null && !permissionIds.isEmpty()) {
                for (Integer permissionId : permissionIds) {
                    SysRolePermission rolePermission = new SysRolePermission();
                    rolePermission.setRoleId(roleId);
                    rolePermission.setPermissionId(permissionId);
                    sysRolePermissionMapper.insert(rolePermission);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("分配权限失败", e);
            throw new RuntimeException("分配权限失败");
        }
    }

    @Override
    public List<SysPermission> getAllValidPermissions() {
        QueryWrapper<SysPermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0)
                .orderByAsc("id");
        return this.list(queryWrapper);
    }
}
