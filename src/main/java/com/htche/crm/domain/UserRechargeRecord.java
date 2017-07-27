package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserRechargeRecord {
    private Integer userRechargeId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 商户订单号
     */
    private String orderNumber;

    /**
     * 充值金额
     */
    private Integer amount;

    /**
     * 状态0-初始 1-成功 10-失败
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;
}