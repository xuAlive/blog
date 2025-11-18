package com.xu.blog.param.vo.ds;

import com.xu.blog.param.deepseek.Completion;
import lombok.Data;

@Data
public class CompletionVO extends Completion {
    private Long dialogueId;
}
