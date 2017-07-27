package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class Notice {
    /**
     * 公告ID
     */
    private Integer noticeId;

    /**
     * 车商ID
     */
    private Integer carDealerId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 配图
     */
    private String picUrl;

    /**
     * 创建时间
     */
    private Date createTime;
}