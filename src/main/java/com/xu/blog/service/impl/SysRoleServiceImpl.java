package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.SysRole;
import com.xu.blog.domain.SysUserRole;
import com.xu.blog.mapper.SysRoleMapper;
import com.xu.blog.mapper.SysUserRoleMapper;
import com.xu.blog.service.SysRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 角色Service实现
 */
@Slf4j
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleMapper sysUserRoleMapper;

    @Override
    public List<SysRole> getRolesByAccount(String account) {
        return baseMapper.selectRolesByAccount(account);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignRoleToUser(String account, Integer roleId) {
        try {
            // 检查是否已存在
            QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account).eq("role_id", roleId);
            SysUserRole existingRole = sysUserRoleMapper.selectOne(queryWrapper);

            if (existingRole != null) {
                return true;
            }

            SysUserRole userRole = new SysUserRole();
            userRole.setAccount(account);
            userRole.setRoleId(roleId);
            return sysUserRoleMapper.insert(userRole) > 0;
        } catch (Exception e) {
            log.error("分配角色失败", e);
            throw new RuntimeException("分配角色失败");
        }
    }

    @Override
    public String getRoleCodeByAccount(String account) {
        List<SysRole> roles = getRolesByAccount(account);
        if (roles == null || roles.isEmpty()) {
            return "GUEST";
        }
        // 返回权限最高的��色
        for (SysRole role : roles) {
            if ("ADMIN".equals(role.getRoleCode())) {
                return "ADMIN";
            }
        }
        for (SysRole role : roles) {
            if ("USER".equals(role.getRoleCode())) {
                return "USER";
            }
        }
        return "GUEST";
    }

    @Override
    public SysRole selectByRoleCode(String roleCode) {
        return baseMapper.selectByRoleCode(roleCode);
    }
}
