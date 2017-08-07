package com.htche.crm.domain.pay;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Created by jankie on 2016/11/3.
 */
@Data
public class WxAccessTokenResult implements Serializable {

    private String access_token;

    private Integer expires_in;

    /**
     * 获取JS票据用的
     */
    private String ticket;
}
