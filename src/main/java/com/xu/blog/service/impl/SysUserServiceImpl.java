package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.dao.SysUserDao;
import com.xu.blog.domain.SysUser;
import com.xu.blog.domain.SysUserLogin;
import com.xu.blog.mapper.SysUserLoginMapper;
import com.xu.blog.mapper.SysUserMapper;
import com.xu.blog.param.UserToken;
import com.xu.blog.param.po.sys.LoginUserPo;
import com.xu.blog.service.SysUserService;
import com.xu.blog.utils.JWTUtil;
import com.xu.blog.utils.response.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
* @author xubaolin
* @description 针对表【sys_user(系统用户表)】的数据库操作Service实现
* @createDate 2024-01-21 12:53:40
*/
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService{
    @Autowired
    private SysUserDao userDao;


    @Override
    public Response login(LoginUserPo po, HttpServletRequest servletRequest) {
        //判断对象是否为空
        if(po == null){
            return Response.error("登录信息不能为空");
        }
        // TODO 验证码是否正确
        if (StringUtils.isEmpty(po.getAccount()) || StringUtils.isEmpty(po.getPassword())){
            return Response.error("用户名或密码不能为空");
        }
        //验证账号是否存在
        SysUser sysUser = baseMapper.selectUser(po.getAccount(),null,null);
        if (Objects.isNull(sysUser)){
            return Response.error("账号不存在");
        }
        //验证密码是否正确
        if (!sysUser.getPassword().equals(po.getPassword())){
            return Response.error("密码错误");
        }
        //登陆成功 记录登陆用户的ip
        String userIP = servletRequest.getHeader("X-Forwarded-For");
        if (userIP == null) {
            userIP = servletRequest.getRemoteAddr();
        }

        SysUserLogin sysUserLogin = new SysUserLogin();
        sysUserLogin.setAccount(po.getAccount());
        sysUserLogin.setIp(userIP);
        userDao.insertUserLogin(sysUserLogin);
        //生成token
        UserToken userToken = new UserToken();
        String token = JWTUtil.createToken(userToken);
        log.info(po.getAccount()+" 登陆成功 ，返回token为 "+token);
        return Response.success(token);
    }

    @Override
    public Response register(LoginUserPo po) {
        //判断对象是否为空
        if(po == null){
            return Response.error("注册信息不能为空");
        }
        if (StringUtils.isEmpty(po.getAccount()) || StringUtils.isEmpty(po.getPassword())){
            return Response.error("用户名或密码不能为空");
        }
        //验证账号是否存在
        List<SysUser> sysUsers = baseMapper.selectList(new QueryWrapper<SysUser>().eq("account",po.getAccount()));
        if (!CollectionUtils.isEmpty(sysUsers)){
            return Response.error("账号已存在");
        }
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(po,sysUser);
        int insert = baseMapper.insert(sysUser);
        return Response.checkResult(insert == 1);
    }


}




