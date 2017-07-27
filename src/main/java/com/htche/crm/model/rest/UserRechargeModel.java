package com.htche.crm.model.rest;

import com.htche.crm.domain.RechargeProduct;
import com.htche.crm.domain.User;
import com.htche.crm.domain.pay.UnifiedOrderResult;
import com.htche.crm.model.ApiResult;
import lombok.Data;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.List;

/**
 * Created by lishengxi on 2017/7/19.
 */
public class UserRechargeModel {

    @Data
    public static class RechargeProductList extends ApiResult {

        public List<RechargeProduct> rechargeProductList;
    }

    @Data
    public static class UseRechargeResult extends ApiResult {

        public UnifiedOrderResult unifiedOrderResult;
    }

    @Data
    public static class WxNotifyResult {
        /**
         * 返回状态码
         */
        @XmlAttribute(name = "return_code")
        private String returnCode;

        /**
         * 返回信息
         */
        @XmlAttribute(name = "return_msg")
        private String returnMsg;
    }
}
