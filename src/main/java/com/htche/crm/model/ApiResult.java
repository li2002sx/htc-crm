package com.htche.crm.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by jankie on 16/5/26.
 */
@Getter
@Setter
public class ApiResult {

    /**
     * 0-失败 1-成功 -1-重新登录
     */
    private Integer status;

    /**
     * 错误提示
     */
    private String message;

    /**
     * 服务器时间戳
     */
    private long date;

    public long getDate() {
        return new Date().getTime();
    }
}
