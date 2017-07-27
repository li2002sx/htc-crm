package com.htche.crm.domain.pay;


import com.htche.crm.constants.BusinessType;
import com.htche.crm.constants.RechargeType;
import lombok.Data;

/**
 * Created by jankie on 16/6/17.
 */
@Data
public class BusinessOrderParam {

    /**
     * 订单号
     */
    private String orderNumber;

    /**
     * 订单业务类型
     */
    private BusinessType businessType;

    /**
     * 订单金额
     */
    private int amount;

    /**
     * 支付方式
     */
    private RechargeType rechargeType;

    /**
     * 商品备注
     */
    private String Remark;

    /**
     * 用户IP地址
     */
    private String ip;

    /**
     * 用户标识
     */
    private String openId;
}
