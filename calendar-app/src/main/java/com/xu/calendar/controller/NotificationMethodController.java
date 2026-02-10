package com.xu.calendar.controller;

import com.xu.calendar.domain.NotificationMethod;
import com.xu.calendar.service.NotificationMethodService;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通知方式接口
 */
@RestController
@RequestMapping("/calendar/notification-method")
public class NotificationMethodController {

    private final NotificationMethodService notificationMethodService;

    public NotificationMethodController(NotificationMethodService notificationMethodService) {
        this.notificationMethodService = notificationMethodService;
    }

    /**
     * 获取所有启用的通知方式
     */
    @GetMapping("/list")
    public Response<List<NotificationMethod>> getNotificationMethods() {
        List<NotificationMethod> methods = notificationMethodService.getActiveMethods();
        return Response.success(methods);
    }
}
