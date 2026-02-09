package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.Miniapp;
import com.xu.blog.mapper.MiniappMapper;
import com.xu.blog.service.MiniappService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 小程序Service实现
 */
@Service
public class MiniappServiceImpl extends ServiceImpl<MiniappMapper, Miniapp> implements MiniappService {

    @Override
    public List<Miniapp> getValidList() {
        QueryWrapper<Miniapp> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_delete", 0)
                .orderByAsc("sort_order");
        return this.list(queryWrapper);
    }
}
