package com.xu.calendar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.calendar.domain.Reminder;
import org.apache.ibatis.annotations.Mapper;

/**
 * 提醒 Mapper
 */
@Mapper
public interface ReminderMapper extends BaseMapper<Reminder> {
}
