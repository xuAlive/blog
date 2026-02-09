package com.xu.schedule.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.schedule.domain.Shift;

import java.util.List;

/**
 * 班次服务接口
 */
public interface ShiftService extends IService<Shift> {

    /**
     * 获取所有启用的班次
     */
    List<Shift> getActiveShifts();
}
