package com.xu.blog.dao;

import com.xu.blog.mapper.BlogArticleInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 博客文章数据管理dao
 */
@Component
public class BlogDao {
    @Autowired
    private BlogArticleInfoMapper blogArticleInfoMapper;

    /**
     * 删除博客文章信息
     * @param account
     * @return
     */
    public Boolean deleteBlogAccount(String account){
        blogArticleInfoMapper.deleteByAccount(account);
        return true;
    }
}
