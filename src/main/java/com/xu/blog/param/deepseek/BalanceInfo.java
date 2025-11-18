package com.xu.blog.param.deepseek;

import lombok.Data;

import java.util.List;

/**
 * 查看余额
 */
@Data
public class BalanceInfo {

    private Boolean isAvailable;

    private List<Balance> BalanceInfos;

}
