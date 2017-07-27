package com.htche.crm.domain;

import com.htche.crm.constants.SexType;
import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户头像
     */
    private String userPic;

    /**
     * 性别 0-女 1-男
     */
    private Integer sex;

    public String getSexName() {
        return SexType.getName(sex);
    }

    /**
     * 状态 1-正常 10 -回收站
     */
    private Integer status;

    /**
     * 登陆token
     */
    private String token;

    /**
     * 最后登录时间
     */
    private Date lastLoginTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 行业类型
     */
    private Integer industryId;

    /**
     * 公司
     */
    private String company;

    /**
     * 职位
     */
    private String position;

    /**
     * 省份ID
     */
    private Integer provinceId;

    /**
     * 城市ID
     */
    private Integer cityId;

    /**
     * 地址
     */
    private String address;

    /**
     * 微信号
     */
    private String wxCode;

    /**
     * 账户到期时间
     */
    private Date expireTime;

    //辅助字段

    /**
     * 行业类型
     */
    private String industryName;

    /**
     * 省份ID
     */
    private String provinceName;

    /**
     * 城市ID
     */
    private String cityName;
}