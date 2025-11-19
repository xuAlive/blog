package com.xu.blog.controller;

import com.xu.blog.ai.deepseek.DSMamage;
import com.xu.blog.param.po.deepseek.DialogueInfoPO;
import com.xu.blog.service.DeepseekDialogueInfoService;
import com.xu.blog.utils.SessionUtil;
import com.xu.blog.utils.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/blog/ds")
@RestController
public class DeepseekController {
    @Autowired
    private DSMamage dSMamage;

    @Autowired
    private DeepseekDialogueInfoService deepseekDialogueInfoService;

    @PostMapping("/sendCompletion")
    public Response sendCompletion(@RequestBody DialogueInfoPO po){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        po.setAccount(account);
        return Response.success(dSMamage.sendCompletion(po));
    }

    @GetMapping("/getCompletionList")
    public Response getCompletion(@RequestParam("dialogueId") Long dialogueId){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        return Response.success(deepseekDialogueInfoService.getDialogueInfo(dialogueId, account));
    }

    @GetMapping("/getCompletionHistoryList")
    public Response getCompletionHistoryList(){
        // 从ThreadLocal获取当前登录用户账号，无需传递HttpServletRequest
        String account = SessionUtil.getCurrentAccount();
        return Response.success(deepseekDialogueInfoService.getCompletionHistoryList(account));
    }
}
