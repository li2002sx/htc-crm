package com.htche.crm.domain;

import lombok.Data;

@Data
public class CarModuleInfo {
    /**
     * 模块信息ID
     */
    private Integer moduleInfoId;

    /**
     * 模块ID
     */
    private Integer moduleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 配图1
     */
    private String picUrl;

    /**
     * 颜色
     */
    private String color;

    /**
     * 连接地址
     */
    private String url;

    /**
     * 排序号
     */
    private Integer orderNo;

    /**
     * 状态 1-正常 10-回收站
     */
    private Integer status;
}