package com.htche.crm.domain;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CarInfo {
    /**
     * 车ID
     */
    private Integer carInfoId;

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
     * 配置信息
     */
    private String configure;

    /**
     * 车架号
     */
    private String frame;

    /**
     * 外观颜色
     */
    private String outColor;

    /**
     * 内饰颜色
     */
    private String inColor;

    /**
     * 车源状态
     */
    private Integer sourceId;

    /**
     * 手续状态
     */
    private Integer procedureId;

    /**
     * 省份ID
     */
    private Integer provinceId;

    /**
     * 城市ID
     */
    private Integer cityId;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 年份
     */
    private Integer year;

    /**
     * 月份
     */
    private Integer month;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 价格占比
     */
    private Integer priceRatio;
}