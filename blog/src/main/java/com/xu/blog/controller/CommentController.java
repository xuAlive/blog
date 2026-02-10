package com.xu.blog.controller;

import com.xu.blog.param.po.blog.CommentListPo;
import com.xu.blog.param.po.blog.CommentPo;
import com.xu.blog.service.BlogCommentService;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

/**
 * 评论 Controller
 */
@RequestMapping("/blog/comment")
@RestController
public class CommentController {

    private final BlogCommentService commentService;

    public CommentController(BlogCommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 添加评论或回复
     */
    @PostMapping("/add")
    public Response addComment(@RequestBody CommentPo po) {
        return commentService.addComment(po);
    }

    /**
     * 获取文章的评论列表
     */
    @GetMapping("/listByArticle")
    public Response listByArticle(CommentListPo po) {
        return commentService.listCommentsByArticle(po);
    }

    /**
     * 获取最新评论列表
     */
    @GetMapping("/listLatest")
    public Response listLatest(@RequestParam(value = "limit", defaultValue = "10") Integer limit) {
        return commentService.listLatestComments(limit);
    }

    /**
     * 删除评论
     */
    @PostMapping("/delete")
    public Response deleteComment(@RequestParam("id") Integer id, @RequestParam("account") String account) {
        return commentService.deleteComment(id, account);
    }
}
