package com.htche.crm.domain;

import lombok.Data;

@Data
public class SmsConf {
    /**
     * 短信ID
     */
    private Integer smsId;

    /**
     * 车商ID
     */
    private Integer carDealerId;

    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 发送时间-小时
     */
    private Integer sendHour;

    /**
     * 发送时间-分
     */
    private Integer sendMinute;
}