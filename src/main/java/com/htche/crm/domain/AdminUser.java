package com.htche.crm.domain;

import lombok.Data;

import java.util.Date;

@Data
public class AdminUser {
    /**
     * 后台管理员ID
     */
    private Integer adminUserId;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String userPic;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 扩展字段
     */
    private String companyName;
}