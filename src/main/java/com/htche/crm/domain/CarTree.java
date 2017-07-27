package com.htche.crm.domain;

import lombok.Data;

@Data
public class CarTree {
    /**
     * 编号
     */
    private Integer id;

    /**
     * 父级ID
     */
    private Integer pId;

    /**
     * 名称
     */
    private String name;

    /**
     * 状态1-正常，10-删除
     */
    private Integer status;
}