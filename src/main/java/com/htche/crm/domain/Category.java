package com.htche.crm.domain;

import lombok.Data;

@Data
public class Category {
    /**
     * 分类ID
     */
    private Integer categoryId;

    /**
     * 分类类型 1-规格，2-车源状态，3-手续状态
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;
}