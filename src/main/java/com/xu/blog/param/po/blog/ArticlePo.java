package com.xu.blog.param.po.blog;

import com.xu.blog.param.PageParam;
import lombok.Data;

import java.util.Date;

@Data
public class ArticlePo extends PageParam {
    /**
     * 账号
     */
    private String account;
    /**
     * 标题
     */
    private String title;

}
