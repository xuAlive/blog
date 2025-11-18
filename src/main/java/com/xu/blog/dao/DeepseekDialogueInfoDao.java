package com.xu.blog.dao;

import com.xu.blog.mapper.DeepseekDialogueInfoMapper;
import com.xu.blog.param.vo.ds.CompletionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DeepseekDialogueInfoDao {
    @Autowired
    private DeepseekDialogueInfoMapper deepseekDialogueInfoMapper;

    public List<CompletionVO> selectCompletion(Long dialogueId) {
        return deepseekDialogueInfoMapper.selectCompletion(dialogueId);
    }
}
