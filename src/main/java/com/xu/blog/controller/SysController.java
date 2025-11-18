package com.xu.blog.controller;

import com.xu.blog.param.po.sys.LoginUserPo;
import com.xu.blog.param.po.sys.UserInfoPo;
import com.xu.blog.service.SysUserInfoService;
import com.xu.blog.service.SysUserService;
import com.xu.blog.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RequestMapping("/blog/sys")
@RestController
public class SysController {
    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysUserInfoService sysUserInfoService;

    @PostMapping("/login")
    public Response login(@RequestBody LoginUserPo po, HttpServletRequest servletRequest){
        return sysUserService.login(po,servletRequest);
    }

    @PostMapping("/register")
    public Response register(@RequestBody LoginUserPo po){
        return sysUserService.register(po);
    }

    @GetMapping("/getUserInfoByAccount")
    public Response getUserInfoByAccount(@RequestParam(value = "account",required = false) String account){
        return sysUserInfoService.getUserInfoByAccount(account);
    }

    @PostMapping("/updateUserInfo")
    public Response updateUserInfo(@RequestBody UserInfoPo po){
        return sysUserInfoService.updateUserInfo(po);
    }
}
