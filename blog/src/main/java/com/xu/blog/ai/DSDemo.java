package com.xu.blog.ai;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.xu.blog.param.deepseek.BalanceInfo;
import com.xu.blog.param.deepseek.CompletionResponse;
import com.xu.blog.utils.DeepSeekAPIUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xu
 * @date 2020/3/31 17:02
 * @desc deepseek 接入测试
 */
public class DSDemo{

    public static void main(String[] args) throws Exception{
//        List<Map<String, String>> mapList = new ArrayList<>();
//        Map<String, String> mesg1 = new HashMap<>();
//        mesg1.put("role", "user");
//        mesg1.put("content", "我饿了怎么办");
//
//        mapList.add(mesg1);
//
//        // 构造请求体
//        JSONObject body = new JSONObject();
//        body.put("model", "deepseek-chat");
//        body.put("messages", mapList); // 使用直接嵌套对象而非字符串
//
//        System.out.println("请求体: " + body.toJSONString());
//        List<CompletionResponse.Choice> completions = DeepSeekAPIUtil.completions(body.toJSONString());
//        System.out.println(completions.get(0).getMessage());
//        System.out.println("响应: " + completions);

        BalanceInfo balance = DeepSeekAPIUtil.balance();
        System.out.println("余额: " + balance);
    }

}
