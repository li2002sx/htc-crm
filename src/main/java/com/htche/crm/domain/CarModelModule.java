package com.htche.crm.domain;

import lombok.Data;

import java.util.List;

@Data
public class CarModelModule {
    /**
     * 模块ID
     */
    private Integer moduleId;

    /**
     * 车型ID
     */
    private Integer carModelId;

    /**
     * 模块标题
     */
    private String title;

    /**
     * 标题字体大小
     */
    private Integer fontSize;

    /**
     * 标题字体颜色
     */
    private String color;

    /**
     * 排序号
     */
    private Integer orderNo;

    /**
     * 状态 1-正常 10-回收站
     */
    private Integer status;

    /**
     * 附加属性，模块信息集合
     */
    private List<CarModuleInfo> carModuleInfos;
}