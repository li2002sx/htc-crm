package com.htche.crm.domain;

import lombok.Data;

@Data
public class Contact {
    /**
     * 通讯录ID
     */
    private Integer contactId;

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
     * 主营业务
     */
    private Integer businessId;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

}