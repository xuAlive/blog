package com.xu.blog.controller.sys;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.blog.domain.SysRole;
import com.xu.blog.domain.SysUser;
import com.xu.blog.service.SysRoleService;
import com.xu.blog.service.SysUserService;
import com.xu.common.utils.SessionUtil;
import com.xu.common.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * 用户管理Controller
 */
@Slf4j
@RequestMapping("/blog/user")
@RestController
public class UserController {

    private final SysUserService sysUserService;
    private final SysRoleService sysRoleService;

    public UserController(SysUserService sysUserService, SysRoleService sysRoleService) {
        this.sysUserService = sysUserService;
        this.sysRoleService = sysRoleService;
    }

    /**
     * 验证手机号，将用户从GUEST升级为USER
     * @param phone 手机号
     * @param verifyCode 验证码（这里简化处理，实际应该先发送验证码到手机）
     */
    @PostMapping("/verifyPhone")
    @Transactional(rollbackFor = Exception.class)
    public Response verifyPhone(@RequestParam("phone") String phone,
                                 @RequestParam("verifyCode") String verifyCode) {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }

        // TODO: 实际应该验证验证码是否正确
        // 这里简化处理，假设验证码正确

        try {
            // 更新用户手机号和验证状态
            SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account", account));
            if (user == null) {
                return Response.error("用户不存在");
            }

            if (user.getPhoneVerified() == 1) {
                return Response.error("手机号已验证");
            }

            user.setPhone(phone);
            user.setPhoneVerified(1);
            boolean updateResult = sysUserService.updateById(user);

            if (updateResult) {
                // 分配USER角色
                SysRole userRole = sysRoleService.selectByRoleCode("USER");
                if (userRole != null) {
                    sysRoleService.assignRoleToUser(account, userRole.getId());
                    log.info("用户{}验证手机号成功，升级为USER角色", account);
                    return Response.success("手机号验证成功，已升级为正式用户");
                } else {
                    log.warn("USER角色不存在，请检查数据库");
                    return Response.error("系统配置错误");
                }
            }
            return Response.error("验证失败");
        } catch (Exception e) {
            log.error("验证手机号失败", e);
            throw new RuntimeException("验证手机号失败");
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/getCurrentUser")
    public Response getCurrentUser() {
        String account = SessionUtil.getCurrentAccount();
        if (account == null) {
            return Response.error("未登录");
        }

        SysUser user = sysUserService.getOne(new QueryWrapper<SysUser>().eq("account", account));
        if (user != null) {
            // 不返回密码
            user.setPassword(null);
            return Response.success(user);
        }
        return Response.error("用户不存在");
    }
}
