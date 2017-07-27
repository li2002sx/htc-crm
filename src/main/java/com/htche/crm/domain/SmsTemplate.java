package com.htche.crm.domain;

import lombok.Data;

@Data
public class SmsTemplate {
    /**
     * 短信模板ID
     */
    private Integer templateId;

    /**
     * 模板类型1-祝福，2-推广
     */
    private Integer templateType;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态 1-正常，10-停用
     */
    private Integer status;
}