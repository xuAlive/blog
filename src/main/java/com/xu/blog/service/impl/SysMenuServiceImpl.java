package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.SysMenu;
import com.xu.blog.domain.SysRoleMenu;
import com.xu.blog.mapper.SysMenuMapper;
import com.xu.blog.mapper.SysRoleMenuMapper;
import com.xu.blog.service.SysMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单Service实现
 */
@Slf4j
@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService {

    @Autowired
    private SysRoleMenuMapper sysRoleMenuMapper;

    @Override
    public List<SysMenu> getMenuTreeByAccount(String account) {
        List<SysMenu> menus = baseMapper.selectMenusByAccount(account);
        return buildMenuTree(menus, 0);
    }

    @Override
    public List<SysMenu> getAllMenuTree() {
        List<SysMenu> menus = baseMapper.selectAllEnabledMenus();
        return buildMenuTree(menus, 0);
    }

    @Override
    public boolean saveMenu(SysMenu menu) {
        return this.save(menu);
    }

    @Override
    public boolean updateMenu(SysMenu menu) {
        return this.updateById(menu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteMenu(Integer menuId) {
        // 逻辑删除
        SysMenu menu = this.getById(menuId);
        if (menu != null) {
            menu.setIsDelete(1);
            return this.updateById(menu);
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean assignMenusToRole(Integer roleId, List<Integer> menuIds) {
        try {
            // 先删除原有的菜单分配
            QueryWrapper<SysRoleMenu> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("role_id", roleId);
            sysRoleMenuMapper.delete(deleteWrapper);

            // 分配新菜单
            if (menuIds != null && !menuIds.isEmpty()) {
                for (Integer menuId : menuIds) {
                    SysRoleMenu roleMenu = new SysRoleMenu();
                    roleMenu.setRoleId(roleId);
                    roleMenu.setMenuId(menuId);
                    sysRoleMenuMapper.insert(roleMenu);
                }
            }
            return true;
        } catch (Exception e) {
            log.error("分配菜单失败", e);
            throw new RuntimeException("分配菜单失败");
        }
    }

    /**
     * 构建菜单树
     */
    private List<SysMenu> buildMenuTree(List<SysMenu> menus, Integer parentId) {
        List<SysMenu> tree = new ArrayList<>();
        for (SysMenu menu : menus) {
            if (menu.getParentId().equals(parentId)) {
                List<SysMenu> children = buildMenuTree(menus, menu.getId());
                menu.setChildren(children);
                tree.add(menu);
            }
        }
        return tree;
    }
}
