package com.xu.blog.param.po.blog;

import com.xu.blog.param.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 评论列表查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommentListPo extends PageParam {

    /**
     * 文章ID
     */
    private Integer articleId;
}
