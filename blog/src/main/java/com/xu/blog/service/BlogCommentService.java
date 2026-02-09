package com.xu.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.blog.domain.BlogComment;
import com.xu.blog.param.po.blog.CommentListPo;
import com.xu.blog.param.po.blog.CommentPo;
import com.xu.common.response.Response;

/**
 * 博客评论 Service 接口
 */
public interface BlogCommentService extends IService<BlogComment> {

    /**
     * 添加评论或回复
     * @param po 评论参数
     * @return 响应结果
     */
    Response addComment(CommentPo po);

    /**
     * 获取文章的评论列表（树形结构）
     * @param po 查询参数
     * @return 评论树形列表
     */
    Response listCommentsByArticle(CommentListPo po);

    /**
     * 获取最新评论列表（首页展示）
     * @param limit 数量限制
     * @return 最新评论列表
     */
    Response listLatestComments(Integer limit);

    /**
     * 删除评论
     * @param id 评论ID
     * @param account 账号（验证权限）
     * @return 响应结果
     */
    Response deleteComment(Integer id, String account);
}
