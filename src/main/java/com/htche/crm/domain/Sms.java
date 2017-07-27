package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Sms {
    /**
     * 发送短信ID
     */
    private Integer smsId;

    /**
     * 车主ID
     */
    private Integer carOwnerId;

    /**
     * 使用模板
     */
    private Integer templateId;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 发送内容
     */
    private String content;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * 返回码
     */
    private String code;

    /**
     * 返回消息ID
     */
    private String huyiSmsId;

    /**
     * 消息创建时间
     */
    private Date createTime;
}