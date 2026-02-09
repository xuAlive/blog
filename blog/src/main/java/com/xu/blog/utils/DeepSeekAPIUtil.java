package com.xu.blog.utils;

import com.alibaba.fastjson2.JSON;
import com.xu.blog.param.deepseek.BalanceInfo;
import com.xu.blog.param.deepseek.CompletionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DeepSeekAPIUtil {

    private static String url;
    private static String key;

    public static final String MODEL_V3 = "deepseek-chat";       // DeepSeek-V3
    public static final String MODEL_R1 = "deepseek-reasoner";   // DeepSeek-R1

    @Value("${deepseek.api.url:https://api.deepseek.com/}")
    public void setUrl(String url) {
        DeepSeekAPIUtil.url = url;
    }

    @Value("${deepseek.api.key:}")
    public void setKey(String key) {
        DeepSeekAPIUtil.key = key;
    }

    public static BalanceInfo balance() {
        try {
            var requestUrl = url + "user/balance";
            var headers = createHeaders();
            var balance = HttpUtil.sendGet(requestUrl, headers);
            return JSON.parseObject(balance, BalanceInfo.class);
        } catch (Exception e) {
            log.error("获取余额失败", e);
        }
        return null;
    }

    public static List<CompletionResponse.Choice> completions(String message) {
        try {
            var requestUrl = url + "chat/completions";
            log.debug("DeepSeek API URL: {}", requestUrl);
            var headers = createHeaders();
            var completion = HttpUtil.sendPost(requestUrl, message, headers);
            var completionResponse = JSON.parseObject(completion, CompletionResponse.class);
            return completionResponse.getChoices();
        } catch (Exception e) {
            log.error("对话请求失败", e);
        }
        return null;
    }

    private static Map<String, String> createHeaders() {
        var headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Authorization", "Bearer " + key);
        return headers;
    }
}
