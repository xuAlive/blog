package com.xu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.domain.SysRolePermission;
import org.apache.ibatis.annotations.Param;

/**
 * 角色权限关联Mapper
 */
public interface SysRolePermissionMapper extends BaseMapper<SysRolePermission> {

    /**
     * 删除角色的所有权限
     */
    int deleteByRoleId(@Param("roleId") Integer roleId);
}
