package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CarDealer {
    /**
     * 车商ID
     */
    private Integer carDealerId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 省份ID
     */
    private Integer provinceId;

    /**
     * 城市ID
     */
    private Integer cityId;

    /**
     * 联系人
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 主营车型
     */
    private String business;

    /**
     * 注册时间
     */
    private Date registTime;

    /**
     * 到期时间
     */
    private Date expireTime;

    /**
     * 短信数量
     */
    private Integer smsNum;

    /**
     * 已发送短信数量
     */
    private Integer sendSmsNum;

    /**
     * 短信APPID
     */
    private String appId;

    /**
     * 短信APPKEY
     */
    private String appKey;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;
}