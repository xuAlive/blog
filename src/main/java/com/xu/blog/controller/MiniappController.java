package com.xu.blog.controller;

import com.xu.blog.domain.Miniapp;
import com.xu.blog.service.MiniappService;
import com.xu.blog.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 小程序Controller
 */
@RequestMapping("/blog/miniapp")
@RestController
public class MiniappController {

    @Autowired
    private MiniappService miniappService;

    /**
     * 获取小程序列表
     */
    @GetMapping("/list")
    public Response getList() {
        List<Miniapp> list = miniappService.getValidList();
        return Response.success(list);
    }
}
