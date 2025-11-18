package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.dao.BlogDao;
import com.xu.blog.dao.SysUserDao;
import com.xu.blog.domain.SysUser;
import com.xu.blog.domain.SysUserInfo;
import com.xu.blog.param.po.sys.UserInfoPo;
import com.xu.blog.param.vo.sys.UserInfoVo;
import com.xu.blog.service.SysUserInfoService;
import com.xu.blog.mapper.SysUserInfoMapper;
import com.xu.blog.utils.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
* @author xubaolin
* @description 针对表【sys_user_info(系统用户详情表)】的数据库操作Service实现
* @createDate 2024-01-21 12:53:40
*/
@Service
public class SysUserInfoServiceImpl extends ServiceImpl<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService{

    @Autowired
    private SysUserDao userDao;
    @Autowired
    private BlogDao blogDao;

    @Override
    public Response getUserInfoByAccount(String account) {
        //判断账号是否为空
        if (StringUtils.isEmpty(account)){
            return Response.error("账号不能为空");
        }
        //验证账号是否存在
        SysUser sysUser = userDao.existSysUser(account);
        if (Objects.isNull(sysUser)){
            return Response.error("账号不存在，请检查账号是否正确");
        }
        //获取用户信息,如果用户信息为空，新建一个用户信息，赋值账号信息返回
        UserInfoVo userInfoVo = baseMapper.selectByAccount(account);
        if (Objects.isNull(userInfoVo)){
            userInfoVo = new UserInfoVo();
            userInfoVo.setAccount(account);
        }
        return Response.success(userInfoVo);
    }

    @Override
    public Response updateUserInfo(UserInfoPo po) {
        if (Objects.isNull(po)){
            return Response.error("参数不能为空");
        }
        if (StringUtils.isEmpty(po.getAccount())){
            return Response.error("账号不能为空");
        }
        SysUserInfo sysUserInfo = new SysUserInfo();
        BeanUtils.copyProperties(po,sysUserInfo);
        baseMapper.update(sysUserInfo,new QueryWrapper<SysUserInfo>().eq("account",po.getAccount()));
        return Response.success();
    }

    @Transactional
    @Override
    public Response deleteAccount(String account) {
        if (StringUtils.isEmpty(account)){
            return Response.error("账号不能为空");
        }
        // 先删除账号 ，再删除博客文章
        userDao.deleteAccount(account);
        blogDao.deleteBlogAccount(account);
        return Response.success();
    }
}




