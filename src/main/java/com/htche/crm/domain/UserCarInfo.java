package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserCarInfo {
    /**
     * 车ID
     */
    private Integer userCarInfoId;

    /**
     * 车型ID
     */
    private Integer carModelId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 价格
     */
    private String price;

    /**
     * 配置信息
     */
    private String configure;

    /**
     * 车架号
     */
    private String frame;

    /**
     * 备注
     */
    private String remark;

    /**
     * 状态 1-正常 10-回收站
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;
}