package com.xu.blog.mapper;

import com.xu.blog.domain.DeepseekDialogueInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xu.blog.param.vo.ds.CompletionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author xubaolin
* @description 针对表【deepseek_dialogue_info(Dee-seek对话信息)】的数据库操作Mapper
* @createDate 2025-03-10 17:28:24
* @Entity com.xu.blog.domain.DeepseekDialogueInfo
*/
public interface DeepseekDialogueInfoMapper extends BaseMapper<DeepseekDialogueInfo> {

    List<CompletionVO> selectCompletion(@Param("dialogueId") Long dialogueId);
}




