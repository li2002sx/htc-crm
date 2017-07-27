package com.htche.crm.websocket;

import lombok.Getter;

/**
 * Created by jankie on 2017/5/26.
 */
@Getter
public class WiselyResponse {

    private String responseMessage;

    public WiselyResponse(String responseMessage) {
        this.responseMessage = responseMessage;
    }
}
