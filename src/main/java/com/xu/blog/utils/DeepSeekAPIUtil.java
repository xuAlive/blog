package com.xu.blog.utils;

import com.alibaba.fastjson.JSON;
import com.xu.blog.param.deepseek.BalanceInfo;
import com.xu.blog.param.deepseek.Completion;
import com.xu.blog.param.deepseek.CompletionResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class DeepSeekAPIUtil {
    public static String URL = "https://api.deepseek.com/";
    public static String KEY = "sk-f11f5ce0ba6d4073b7f2d6fed6b2936f";
    public static String model1 = "deepseek-chat";// DeepSeek-V3
    public static String model2 = "deepseek-reasoner";//DeepSeek-R1


    public static BalanceInfo balance(){
        try {
            String url = URL + "user/balance";
            Map<String,String> hearder = new HashMap<>();
            hearder.put("Content-Type","application/json");
            hearder.put("Authorization","Bearer "+ KEY);
            String balance = HttpUtil.sendGet(url, hearder);
            BalanceInfo balanceInfo = JSON.parseObject(balance, BalanceInfo.class);
            return balanceInfo;
        } catch (Exception e) {
            log.error("获取余额失败",e);
        }
        return null;
    }

    public static List<CompletionResponse.Choice> completions(String message){
        try {
            String url = URL+"chat/completions";
            System.out.println(url);
            Map<String,String> hearder = new HashMap<>();
            hearder.put("Content-Type","application/json");
            hearder.put("Authorization","Bearer "+ KEY);
            String completion = HttpUtil.sendPost(url, message,hearder);
            CompletionResponse completionResponse = JSON.parseObject(completion, CompletionResponse.class);
            return completionResponse.getChoices();
        } catch (Exception e) {
            log.error("对话请求失败",e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(balance());
    }
}
