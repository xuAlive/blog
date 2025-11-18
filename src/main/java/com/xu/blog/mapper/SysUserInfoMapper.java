package com.xu.blog.mapper;

import com.xu.blog.domain.SysUserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.param.vo.sys.UserInfoVo;
import org.apache.ibatis.annotations.Param;

/**
* @author xubaolin
* @description 针对表【sys_user_info(系统用户详情表)】的数据库操作Mapper
* @createDate 2024-01-21 13:49:27
* @Entity com.xu.blog.domain.SysUserInfo
*/
public interface SysUserInfoMapper extends BaseMapper<SysUserInfo> {

    UserInfoVo selectByAccount(@Param("account") String account);

    void delteUserInfo(@Param("account") String account);
}




