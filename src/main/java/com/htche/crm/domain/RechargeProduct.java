package com.htche.crm.domain;

import lombok.Data;

@Data
public class RechargeProduct {
    /**
     * 产品ID
     */
    private Integer productId;

    /**
     * 名称
     */
    private String name;

    /**
     * 价格
     */
    private Integer price;

    /**
     * 状态 1-正常 10-回收站
     */
    private Integer status;

    /**
     * 增加时间如1D,1M,1Y
     */
    private String time;
}