package com.xu.blog.controller;

import com.xu.blog.param.po.blog.ArticlePo;
import com.xu.blog.param.po.blog.BlogArticlePo;
import com.xu.blog.service.BlogArticleInfoService;
import com.xu.common.response.Response;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blog/article")
@RestController
public class ArticleController {

    private final BlogArticleInfoService articleInfoService;

    public ArticleController(BlogArticleInfoService articleInfoService) {
        this.articleInfoService = articleInfoService;
    }

    @PostMapping("/createOrUpdateArticle")
    public Response createOrUpdateArticle(@RequestBody BlogArticlePo po){
        return articleInfoService.createOrUpdateArticle(po);
    }

    @GetMapping("/listArticle")
    public Response listArticle(ArticlePo po){
        return articleInfoService.listArticle(po);
    }

    @GetMapping("/getArticleById")
    public Response getArticleById(Integer id){
        return articleInfoService.getArticleById(id);
    }
}
