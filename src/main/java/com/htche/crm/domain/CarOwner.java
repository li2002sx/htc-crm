package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CarOwner {
    /**
     * 车主ID
     */
    private Integer carOwnerId;

    /**
     * 车商ID
     */
    private Integer carDealerId;

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
     * 身份证号码
     */
    private String cardNo;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 购车时间
     */
    private Date buyTime;

    /**
     * 购买车型
     */
    private String buyCarModels;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 发送短信时间
     */
    private Date smsTime;
}