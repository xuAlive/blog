package com.xu.blog.param.deepseek;

import lombok.Data;

import java.util.List;


@Data
public class CompletionResponse {
    private String id;
    private String object;
    private int created;
    private String model;
    private List<Choice> choices;
    private Usage usage;
    private String systemFingerprint;
    @Data
    public class Choice {
        private int index;
        private Completion message;
        private String finishReason;
        private String logprobs;
    }
    @Data
    public class Usage {
        private int promptTokens;
        private int completionTokens;
        private int totalTokens;
        private int promptCacheHitTokens;
        private int promptCacheMissTokens;
        private PromptTokensDetail promptTokensDetails;
        @Data
        public class PromptTokensDetail {
            private int cached_tokens;
        }

    }
}
