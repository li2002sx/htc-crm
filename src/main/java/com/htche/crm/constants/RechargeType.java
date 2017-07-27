package com.htche.crm.constants;

import lombok.Getter;

/**
 * Created by jankie on 2017/5/20.
 */
@Getter
public enum RechargeType {

    Wx("微信", 1),
    Alipay("支付宝", 2);

    // 成员变量
    private String name;
    private int index;

    // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    private RechargeType(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (RechargeType c : RechargeType.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
