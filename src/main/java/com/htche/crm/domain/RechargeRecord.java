package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class RechargeRecord {

    /**
     * 充值编号
     */
    private Integer rechargeId;

    /**
     * 金额
     */
    private Integer amount;

    /**
     * 充值流水号
     */
    private String rechargeNumber;

    /**
     * 充值类型 1-微信 2-支付宝
     */
    private Integer rechargeType;

    /**
     * 业务订单号
     */
    private String orderNumber;

    /**
     * 1-虚拟商品
     */
    private Integer businessType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 状态 0-初始 1-成功 10-失败
     */
    private Integer status;

    /**
     * 统一支付流水号，微信使用
     */
    private String payId;

    /**
     * 交易单号
     */
    private String transactionId;
}