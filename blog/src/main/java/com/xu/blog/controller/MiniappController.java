package com.xu.blog.controller;

import com.xu.blog.domain.Miniapp;
import com.xu.blog.service.MiniappService;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序Controller
 */
@RequestMapping("/blog/miniapp")
@RestController
public class MiniappController {

    private final MiniappService miniappService;

    public MiniappController(MiniappService miniappService) {
        this.miniappService = miniappService;
    }

    /**
     * 获取小程序列表
     */
    @GetMapping("/list")
    public Response getList() {
        List<Miniapp> list = miniappService.getValidList();
        return Response.success(list);
    }
}
