package com.xu.blog.param.po.blog;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class BlogArticlePo {

    private Integer id;

    /**
     * 账号
     */
    private String account;

    /**
     * 博客标题
     */
    private String title;

    /**
     * 简介
     */
    private String intro;

    /**
     * 图片json数组，例[,,]
     */
    private String img;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态 0 暂存，1 发布 -1 删除
     */
    private Integer status;

}
