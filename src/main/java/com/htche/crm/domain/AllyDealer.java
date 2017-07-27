package com.htche.crm.domain;

import lombok.Data;

@Data
public class AllyDealer {
    /**
     * 盟商ID
     */
    private Integer allyDealerId;

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
     * 状态 1-正常 10 -回收站
     */
    private Integer status;
}