package com.xu.blog.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xu.blog.domain.SysUser;
import com.xu.blog.domain.SysUserLogin;
import com.xu.blog.mapper.SysUserInfoMapper;
import com.xu.blog.mapper.SysUserLoginMapper;
import com.xu.blog.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 系统用户信息数据库交互层 降低service和其他mapper的依赖
 * 统一管理service和其他的mapper交互 以及其他没有service层的mapper处理
 * 方法需要注释明确，提高代码可阅读性
 */
@Component
public class SysUserDao {
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private SysUserInfoMapper userInfoMapper;
    @Autowired
    private SysUserLoginMapper loginMapper;
    /**
     * 检查用户账号是否存在 ,存在返回账号
     * @param account
     * @return
     */
    public SysUser existSysUser(String account){
        List<SysUser> sysUserList = userMapper.selectList(new QueryWrapper<SysUser>().eq(account, "account"));
        if (CollectionUtils.isEmpty(sysUserList)){
            return null;
        }
        // 一个账号只会对应一条数据
        return sysUserList.get(0);
    }

    public Boolean insertUserLogin(SysUserLogin userLogin){
        int insert = loginMapper.insert(userLogin);
        return insert == 1;
    }

    @Transactional
    public Boolean deleteAccount(String account) {
        userMapper.deleteUser(account);
        userInfoMapper.delteUserInfo(account);
        return true;
    }
}
