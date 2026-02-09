package com.xu.blog.mapper;

import com.xu.blog.domain.SysUserLogin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.param.vo.sys.UserLoginVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author xubaolin
* @description 针对表【sys_user_login(用户登陆信息表)】的数据库操作Mapper
* @createDate 2024-01-21 13:49:27
* @Entity com.xu.blog.domain.SysUserLogin
*/
public interface SysUserLoginMapper extends BaseMapper<SysUserLogin> {

    /**
     * 按账号和IP分组查询登录记录
     * @param account 账号（可选，为null时查询所有账号）
     * @return 登录记录列表
     */
    List<UserLoginVO> selectLoginRecordsGroupByAccountAndIp(@Param("account") String account);
}




