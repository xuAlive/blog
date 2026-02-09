package com.xu.schedule.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.schedule.domain.Schedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 排班记录 Mapper
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {
}
