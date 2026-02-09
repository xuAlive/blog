package com.xu.blog.service;

import com.xu.blog.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.blog.param.po.sys.LoginUserPo;
import com.xu.common.response.Response;

import jakarta.servlet.http.HttpServletRequest;

/**
* @author xubaolin
* @description 针对表【sys_user(系统用户表)】的数据库操作Service
* @createDate 2024-01-21 12:53:40
*/
public interface SysUserService extends IService<SysUser> {

    /**
     * 登陆
     * @param po
     * @return
     */
    Response login(LoginUserPo po, HttpServletRequest servletRequest);
    /**
     * 注册
     * @param po
     * @return
     */
    Response register(LoginUserPo po);
}
