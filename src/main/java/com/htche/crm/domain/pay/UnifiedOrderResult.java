package com.htche.crm.domain.pay;


import lombok.Data;

import java.io.Serializable;

/**
 * Created by jankie on 16/6/17.
 */
@Data
public class UnifiedOrderResult {

    /**
     * 应用ID
     */
    private String appId;

    /**
     * 时间戳
     */
    private long timeStamp;

    /**
     * 预支付交易会话标识
     */
    private String prepayId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 加密类型
     */
    private String signType;

    /**
     * 签名
     */
    private String sign;
}
