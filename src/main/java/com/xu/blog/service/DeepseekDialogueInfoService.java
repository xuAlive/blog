package com.xu.blog.service;

import com.xu.blog.domain.DeepseekDialogueInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xu.blog.param.po.deepseek.DialogueInfoPO;
import com.xu.blog.param.vo.ds.CompletionHistoryVO;
import com.xu.blog.param.vo.ds.CompletionVO;

import java.util.List;

/**
* @author xubaolin
* @description 针对表【deepseek_dialogue_info(Dee-seek对话信息)】的数据库操作Service
* @createDate 2025-03-10 17:28:24
*/
public interface DeepseekDialogueInfoService extends IService<DeepseekDialogueInfo> {
    /**
     * 保存对话信息
     * @param dialogueInfoPO
     * @return
     */
    Long saveDialogueInfo(DialogueInfoPO dialogueInfoPO);

    /**
     * 根据对话id获取每次对话信息
     * @param dialogueId
     * @param account 用户账号
     * @return
     */
    List<CompletionVO> getDialogueInfo(Long dialogueId, String account);

    /**
     * 获取对话历史列表
     * @param account 用户账号
     * @return
     */
    List<CompletionHistoryVO> getCompletionHistoryList(String account);
}
