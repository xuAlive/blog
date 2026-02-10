package com.xu.calendar.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.calendar.domain.Event;
import org.apache.ibatis.annotations.Mapper;

/**
 * 事件 Mapper
 */
@Mapper
public interface EventMapper extends BaseMapper<Event> {
}
