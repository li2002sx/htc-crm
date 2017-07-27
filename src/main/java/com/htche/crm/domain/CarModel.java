package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class CarModel {
    /**
     * 车型ID
     */
    private Integer carModelId;

    /**
     * 规格ID
     */
    private Integer specId;

    /**
     * 品牌ID
     */
    private Integer brandId;

    /**
     * 车系ID
     */
    private Integer seriesId;

    /**
     * 车型ID
     */
    private Integer modelsId;

    /**
     * 车型图片
     */
    private String picUrl;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}