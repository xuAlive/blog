package com.xu.blog.controller;

import com.xu.common.annotation.RequireRole;
import com.xu.blog.domain.SysMenu;
import com.xu.blog.service.SysMenuService;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜单管理Controller
 */
@RequestMapping("/blog/menu")
@RestController
public class MenuController {

    private final SysMenuService sysMenuService;

    public MenuController(SysMenuService sysMenuService) {
        this.sysMenuService = sysMenuService;
    }

    /**
     * 获取当前用户的菜单树
     */
    @GetMapping("/getUserMenus")
    public Response getUserMenus() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }
        List<SysMenu> menuTree = sysMenuService.getMenuTreeByAccount(account);
        return Response.success(menuTree);
    }

    /**
     * 获取所有菜单树（管理员用）
     */
    @GetMapping("/getAllMenus")
    @RequireRole("ADMIN")
    public Response getAllMenus() {
        List<SysMenu> menuTree = sysMenuService.getAllMenuTree();
        return Response.success(menuTree);
    }

    /**
     * 获取菜单列表（平铺，用于分配）
     */
    @GetMapping("/getMenuList")
    @RequireRole("ADMIN")
    public Response getMenuList() {
        List<SysMenu> menus = sysMenuService.list();
        return Response.success(menus);
    }

    /**
     * 新增菜单
     */
    @PostMapping("/add")
    @RequireRole("ADMIN")
    public Response addMenu(@RequestBody SysMenu menu) {
        boolean result = sysMenuService.saveMenu(menu);
        return result ? Response.success() : Response.error("新增菜单失败");
    }

    /**
     * 修改菜单
     */
    @PostMapping("/update")
    @RequireRole("ADMIN")
    public Response updateMenu(@RequestBody SysMenu menu) {
        boolean result = sysMenuService.updateMenu(menu);
        return result ? Response.success() : Response.error("修改菜单失败");
    }

    /**
     * 删除菜单
     */
    @PostMapping("/delete")
    @RequireRole("ADMIN")
    public Response deleteMenu(@RequestParam("menuId") Integer menuId) {
        boolean result = sysMenuService.deleteMenu(menuId);
        return result ? Response.success() : Response.error("删除菜单失败");
    }

    /**
     * 为角色分配菜单
     */
    @PostMapping("/assignToRole")
    @RequireRole("ADMIN")
    public Response assignMenusToRole(@RequestParam("roleId") Integer roleId,
                                       @RequestBody List<Integer> menuIds) {
        boolean result = sysMenuService.assignMenusToRole(roleId, menuIds);
        return result ? Response.success() : Response.error("分配菜单失败");
    }
}
