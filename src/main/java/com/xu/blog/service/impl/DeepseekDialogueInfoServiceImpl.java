package com.xu.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xu.blog.dao.DeepseekDialogueInfoDao;
import com.xu.blog.domain.DeepseekDialogueInfo;
import com.xu.blog.param.po.deepseek.DialogueInfoPO;
import com.xu.blog.param.vo.ds.CompletionVO;
import com.xu.blog.service.DeepseekDialogueInfoService;
import com.xu.blog.mapper.DeepseekDialogueInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Objects;

/**
* @author xubaolin
* @description 针对表【deepseek_dialogue_info(Dee-seek对话信息)】的数据库操作Service实现
* @createDate 2025-03-10 17:28:24
*/
@Slf4j
@Service
public class DeepseekDialogueInfoServiceImpl extends ServiceImpl<DeepseekDialogueInfoMapper, DeepseekDialogueInfo> implements DeepseekDialogueInfoService{

    @Autowired
    private DeepseekDialogueInfoDao deepseekDialogueInfoDao;


    @Override
    public Long saveDialogueInfo(DialogueInfoPO dialogueInfoPO) {
        try {
            if (dialogueInfoPO != null){
                DeepseekDialogueInfo info = new DeepseekDialogueInfo();
                BeanUtils.copyProperties(dialogueInfoPO,info);
                if (Objects.isNull(dialogueInfoPO.getDialogueId())){
                    long millis = System.currentTimeMillis();
                    info.setDialogueId(millis);
                }
                this.save(info);
                return info.getDialogueId();
            }
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return null;
    }

    @Override
    public List<CompletionVO> getDialogueInfo(Long dialogueId) {
        List<CompletionVO> daoList = deepseekDialogueInfoDao.selectCompletion(dialogueId);
        return daoList;
    }

}




