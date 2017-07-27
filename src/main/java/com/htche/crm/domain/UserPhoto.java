package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserPhoto {
    /**
     * 照片ID
     */
    private Integer photoId;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 照片
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