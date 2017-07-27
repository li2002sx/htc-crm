package com.htche.crm.domain;

import lombok.Data;

@Data
public class CarModelTemplate {
    /**
     * 模板ID
     */
    private Integer templateId;

    /**
     * 车型ID
     */
    private Integer carModelId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 背景图
     */
    private String bgPicUrl;

    /**
     * 模块ID集合，逗号分开
     */
    private String moduleIds;

    /**
     * 状态 1-正常 10-回收站
     */
    private Integer status;

    /**
     * 是否默认 0-否，1-是
     */
    private Integer acquiesce;
}