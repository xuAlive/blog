package com.xu.calendar.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.calendar.domain.AppNotification;

import java.util.List;

/**
 * 应用内通知服务接口
 */
public interface AppNotificationService extends IService<AppNotification> {

    /**
     * 获取用户的待显示通知（当前时间之前且未读）
     */
    List<AppNotification> getPendingNotifications(String account);

    /**
     * 标记为已读
     */
    boolean markAsRead(Long id);

    /**
     * 标记全部已读
     */
    boolean markAllAsRead(String account);
}
