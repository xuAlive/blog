package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.domain.BlogArticleInfo;
import com.xu.blog.param.po.blog.ArticlePo;
import com.xu.blog.param.po.blog.BlogArticlePo;
import com.xu.blog.param.vo.blog.BlogArticleVo;
import com.xu.blog.service.BlogArticleInfoService;
import com.xu.blog.mapper.BlogArticleInfoMapper;
import com.xu.common.response.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
* @author xubaolin
* @description 针对表【blog_article_info(博客文章详情表)】的数据库操作Service实现
* @createDate 2024-01-21 12:53:40
*/
@Service
public class BlogArticleInfoServiceImpl extends ServiceImpl<BlogArticleInfoMapper, BlogArticleInfo> implements BlogArticleInfoService{

    @Override
    public Response createOrUpdateArticle(BlogArticlePo po) {
        if (Objects.isNull(po)){
            return Response.error("参数不能为空");
        }
        BlogArticleInfo blogArticleInfo = new BlogArticleInfo();
        BeanUtils.copyProperties(po,blogArticleInfo);
        boolean result = saveOrUpdate(blogArticleInfo);
        return Response.checkResult(result);
    }

    @Override
    public Response deleteArticle(Integer id, String account) {
        BlogArticleInfo blogArticleInfo = new BlogArticleInfo();
        blogArticleInfo.setStatus(-1);
        int update = baseMapper.update(blogArticleInfo, new QueryWrapper<BlogArticleInfo>()
                .eq("id", id)
                .eq("account", account));
        return Response.checkResult(update == 1);
    }

    @Override
    public Response getArticleById(Integer id) {
        BlogArticleInfo blogArticleInfo = baseMapper.selectById(id);
        BlogArticleVo vo = new BlogArticleVo();
        BeanUtils.copyProperties(blogArticleInfo,vo);
        return Response.success(vo);
    }

    @Override
    public Response listArticle(ArticlePo po) {
        if (Objects.isNull(po)){
            return Response.error("参数不能为空");
        }
        Page<BlogArticleInfo> page = new Page<BlogArticleInfo>(po.getPage(),po.getSize());
        QueryWrapper<BlogArticleInfo> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(po.getAccount())){
            queryWrapper.eq("account",po.getAccount());
        } else {
            // 博客广场：不传account时只查询已发布的文章
            queryWrapper.eq("status", 1);
        }
        if (!StringUtils.isEmpty(po.getTitle())){
            queryWrapper.like("title",po.getTitle());
        }
        queryWrapper.orderByDesc("create_time");
        Page<BlogArticleInfo> articleInfoPage = page(page, queryWrapper);
        return Response.success(articleInfoPage);
    }
}




