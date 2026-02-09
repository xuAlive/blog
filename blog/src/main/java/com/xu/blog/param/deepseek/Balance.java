package com.xu.blog.param.deepseek;

import lombok.Data;

@Data
public class Balance {
    /**
     * Possible values: [CNY, USD]
     * 货币，人民币或美元
     */
    private String currency;

    /**
     * 总的可用余额，包括赠金和充值余额
     */
    private String totalBalance;

    /**
     * 未过期的赠金余额
     */
    private String grantedBalance;

    /**
     *
     */
    private String toppedUpBalance;
}
