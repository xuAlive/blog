package com.xu.calendar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.calendar.domain.AppNotification;
import org.apache.ibatis.annotations.Mapper;

/**
 * 应用内通知 Mapper
 */
@Mapper
public interface AppNotificationMapper extends BaseMapper<AppNotification> {
}
