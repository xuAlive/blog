package com.xu.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.domain.SysPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限Mapper
 */
public interface SysPermissionMapper extends BaseMapper<SysPermission> {

    /**
     * 根据用户账号获取权限列表
     */
    List<SysPermission> selectPermissionsByAccount(@Param("account") String account);

    /**
     * 根据角色ID获取权限列表
     */
    List<SysPermission> selectPermissionsByRoleId(@Param("roleId") Integer roleId);
}
