package com.htche.crm.domain;

import lombok.Data;

@Data
public class Region {

    /**
     * 区域编号
     */
    private Integer regionId;

    /**
     * 名称
     */
    private String title;

    /**
     * 父级ID
     */
    private Integer pid;

    /**
     * 深度
     */
    private Integer depth;
}