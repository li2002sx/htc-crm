package com.htche.crm.constants;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by jankie on 2017/5/20.
 */
@Getter
public enum CommonStatus {

    Normal("正常态", 1),
    Deleted("已删除", 10);

    // 成员变量
    private String name;
    private int index;

    // 构造方法，注意：构造方法不能为public，因为enum并不可以被实例化
    private CommonStatus(String name, int index) {
        this.name = name;
        this.index = index;
    }

    // 普通方法
    public static String getName(int index) {
        for (CommonStatus c : CommonStatus.values()) {
            if (c.getIndex() == index) {
                return c.name;
            }
        }
        return null;
    }
}
