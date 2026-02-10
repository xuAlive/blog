package com.xu.calendar.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.calendar.domain.AppNotification;
import com.xu.calendar.mapper.AppNotificationMapper;
import com.xu.calendar.service.AppNotificationService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 应用内通知服务实现
 */
@Service
public class AppNotificationServiceImpl extends ServiceImpl<AppNotificationMapper, AppNotification>
        implements AppNotificationService {

    @Override
    public List<AppNotification> getPendingNotifications(String account) {
        return lambdaQuery()
                .eq(AppNotification::getAccount, account)
                .eq(AppNotification::getIsRead, 0)
                .le(AppNotification::getRemindTime, LocalDateTime.now())
                .orderByDesc(AppNotification::getRemindTime)
                .list();
    }

    @Override
    public boolean markAsRead(Long id) {
        AppNotification notification = new AppNotification();
        notification.setId(id);
        notification.setIsRead(1);
        return updateById(notification);
    }

    @Override
    public boolean markAllAsRead(String account) {
        return lambdaUpdate()
                .eq(AppNotification::getAccount, account)
                .eq(AppNotification::getIsRead, 0)
                .set(AppNotification::getIsRead, 1)
                .update();
    }
}
