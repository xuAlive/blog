package com.xu.blog.dao;

import com.xu.blog.mapper.BlogArticleInfoMapper;
import org.springframework.stereotype.Component;

/**
 * 博客文章数据管理dao
 */
@Component
public class BlogDao {

    private final BlogArticleInfoMapper blogArticleInfoMapper;

    public BlogDao(BlogArticleInfoMapper blogArticleInfoMapper) {
        this.blogArticleInfoMapper = blogArticleInfoMapper;
    }

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
