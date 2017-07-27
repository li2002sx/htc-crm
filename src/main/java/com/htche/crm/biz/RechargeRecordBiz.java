package com.htche.crm.biz;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import com.htche.crm.constants.PayStatus;
import com.htche.crm.constants.RechargeType;
import com.htche.crm.domain.RechargeProduct;
import com.htche.crm.domain.RechargeRecord;
import com.htche.crm.domain.User;
import com.htche.crm.domain.UserRechargeRecord;
import com.htche.crm.domain.pay.BusinessOrderParam;
import com.htche.crm.domain.pay.UnifiedOrderResult;
import com.htche.crm.mapper.RechargeRecordMapper;
import com.htche.crm.util.AmountUtil;
import com.htche.crm.util.DateUtil;
import com.htche.crm.util.RandUtil;
import com.htche.crm.util.ValidateUtil;
import com.htche.crm.util.pay.WeixinPayConfig;
import com.htche.crm.util.pay.WeixinUtil;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jankie on 2017/5/19.
 */
@Component
public class RechargeRecordBiz {

    private static final Logger logger = LoggerFactory.getLogger(RechargeRecordBiz.class);

    static final WeixinPayConfig _WxPayConfig = new WeixinPayConfig();

    @Autowired
    RechargeRecordMapper rechargeRecordMapper;

    @Autowired
    RechargeProductBiz rechargeProductBiz;

    @Autowired
    UserRechargeRecordBiz userRechargeRecordBiz;

    @Autowired
    UserBiz userBiz;

    /**
     * 创建充值流水
     *
     * @param orderParam
     * @return
     */
    public RechargeRecord createRechargeRecord(BusinessOrderParam orderParam) {
        int amount = orderParam.getAmount();

        String orderNumber = orderParam.getOrderNumber();
        //插入充值记录
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByOrderNumber(orderNumber);
        if (rechargeRecord == null || rechargeRecord.getStatus() == PayStatus.Failed.getIndex()) {
            rechargeRecord = new RechargeRecord();
            String rechargeNumber = RandUtil.stringId18();

            rechargeRecord.setOrderNumber(orderNumber);
            rechargeRecord.setRechargeNumber(rechargeNumber);
            rechargeRecord.setAmount(amount);
            RechargeType rechargeType = orderParam.getRechargeType();
            rechargeRecord.setRechargeType(rechargeType.getIndex());
            rechargeRecord.setBusinessType(orderParam.getBusinessType().getIndex());
            rechargeRecord.setCreateTime(new Date());
            rechargeRecord.setStatus(PayStatus.Init.getIndex());
            int rechargeId = rechargeRecordMapper.insertSelective(rechargeRecord);
            rechargeRecord.setRechargeId(rechargeId);

        } else if (rechargeRecord.getStatus() == PayStatus.Success.getIndex()) {
            throw new RuntimeException("该订单已经支付成功，请不要重复支付");
        }

        return rechargeRecord;
    }

    /**
     * 统一下单
     *
     * @return
     */
    public UnifiedOrderResult unifiedOrder(BusinessOrderParam orderParam) {

        UnifiedOrderResult unifiedOrderResult = null;

        boolean flag;
        RechargeRecord rechargeRecord = this.createRechargeRecord(orderParam);
        int rechargeId = rechargeRecord.getRechargeId();
        int amount = rechargeRecord.getAmount();
        String rechargeNumber = rechargeRecord.getRechargeNumber();
        if (rechargeId > 0) {
            //发起第三方充值统一下单
            WXPay wxpay = new WXPay(_WxPayConfig);

            Map<String, String> data = new HashMap<String, String>();
            data.put("body", orderParam.getRemark());
            data.put("out_trade_no", orderParam.getOrderNumber());
            data.put("device_info", "WEB");
            data.put("fee_type", "CNY");
            data.put("total_fee", String.valueOf(amount));
            data.put("spbill_create_ip", orderParam.getIp());
            data.put("notify_url", WeixinUtil._WxNotifyUrl);
            data.put("trade_type", "JSAPI");  // 此处指定为公众号支付
            data.put("openid", orderParam.getOpenId());

            try {
                Map<String, String> resp = wxpay.unifiedOrder(data);
                logger.info("unified-resp:{}", JSON.toJSONString(resp));
                String returnCode = resp.get("return_code");
                String resultCode = resp.get("result_code");
                if (returnCode.equals("SUCCESS") && resultCode.equals("SUCCESS")) {
                    String prepayId = resp.get("prepay_id");
                    Map<String, Object> map = new HashMap<>();
                    map.put("rechargeId", rechargeId);
                    map.put("payId", prepayId);
                    int effectCount = rechargeRecordMapper.updatePayIdByRechargeId(map);
                    if (effectCount > 0) {
                        unifiedOrderResult = new UnifiedOrderResult();
                        unifiedOrderResult.setAppId(resp.get("appid"));
                        unifiedOrderResult.setTimeStamp(new Date().getTime() / 1000L);
                        unifiedOrderResult.setNonceStr(resp.get("nonce_str"));
                        unifiedOrderResult.setPrepayId(prepayId);
                        unifiedOrderResult.setSignType("MD5");
                        unifiedOrderResult.setSign(resp.get("sign"));
                    } else {
                        throw new RuntimeException("更新微信支付订单失败");
                    }
                } else {
                    String returnMsg = StringUtils.defaultIfEmpty(resp.get("return_msg"), "");
                    String resultMsg = StringUtils.defaultIfEmpty(resp.get("result_msg"), "");
                    throw new RuntimeException(returnMsg + resultMsg);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new RuntimeException("创建充值流水失败");
        }
        return unifiedOrderResult;
    }

    /**
     * 微信支付结果通知
     *
     * @param notifyData
     */
    public boolean wxNotify(String notifyData) {
        boolean paySucc = false;
        WXPay wxpay = new WXPay(_WxPayConfig);
        try {
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(notifyData);  // 转换成map
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {
                // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户侧订单状态从退款改成支付成功
                String orderNumber = notifyMap.get("out_trade_no");
                RechargeRecord rechargeRecord = rechargeRecordMapper.selectByOrderNumber(orderNumber);
                if (rechargeRecord != null) {
                    paySucc = this.handlerWxNotify(rechargeRecord.getRechargeId(), notifyMap);
                } else {
                    throw new RuntimeException("没有找到对应的流水");
                }
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                throw new RuntimeException("签名校验错误");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return paySucc;
    }

    /**
     * 微信订单支付结果查询
     *
     * @param prepayId
     * @return
     */
    public boolean wxOrderQuery(String prepayId) {
        boolean paySucc = false;
        if (StringUtils.isNotEmpty(prepayId)) {
            RechargeRecord rechargeRecord = rechargeRecordMapper.selectByPayId(prepayId);
            if (rechargeRecord != null) {
                int rechargeId = rechargeRecord.getRechargeId();
                if (rechargeRecord.getStatus() == PayStatus.Init.getIndex()) {
                    WXPay wxpay = new WXPay(_WxPayConfig);

                    Map<String, String> data = new HashMap<String, String>();
                    data.put("transaction_id", prepayId);

                    try {
                        Map<String, String> resp = wxpay.orderQuery(data);
                        paySucc = this.handlerWxNotify(rechargeId, resp);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                } else if (rechargeRecord.getStatus() == PayStatus.Success.getIndex()) {
                    paySucc = true;
                }
            } else {
                throw new RuntimeException("没有找到对应的流水");
            }
        } else {
            throw new RuntimeException("预付ID不能为空");
        }
        return paySucc;
    }

    /**
     * 私有方法
     *
     * @param rechargeId
     * @param resp
     */
    private boolean handlerWxNotify(int rechargeId, Map<String, String> resp) {
        boolean paySucc = false;
        logger.info("query-resp:{}", JSON.toJSONString(resp));
        String returnCode = resp.get("return_code");
        String resultCode = resp.get("result_code");
        String tradeState = resp.get("trade_state");
        if (returnCode.equals("SUCCESS") && resultCode.equals("SUCCESS")) {
            if (tradeState.equals("SUCCESS")) {
                paySucc = this.updatePayResult(rechargeId, PayStatus.Success.getIndex(), resp);
            } else if (tradeState.equals("PAYERROR")) { //支付失败(其他原因，如银行返回失败)
                paySucc = this.updatePayResult(rechargeId, PayStatus.Failed.getIndex(), resp);
            }
        } else {
            paySucc = this.updatePayResult(rechargeId, PayStatus.Failed.getIndex(), resp);
//            String returnMsg = StringUtils.defaultIfEmpty(resp.get("return_msg"), "");
//            String resultMsg = StringUtils.defaultIfEmpty(resp.get("err_code_des"), "");
//            String tradeStateDesc = StringUtils.defaultIfEmpty(resp.get("trade_state_desc"), "");
//            throw new RuntimeException(returnMsg + resultMsg);
        }
        return paySucc;
    }

    /**
     * 通知成功业务处理
     *
     * @param rechargeId
     * @param status
     * @param resp
     */
    @Transactional
    private boolean updatePayResult(int rechargeId, int status, Map<String, String> resp) {
        boolean paySucc = false;
        RechargeRecord rechargeRecord = rechargeRecordMapper.selectByPrimaryKey(rechargeId);
        if (rechargeRecord != null) {
            Map<String, Object> map = new HashMap<>();
            Date finishTime = DateUtil.strToDate(resp.get("time_end"));

            map.put("rechargeId", rechargeId);
            map.put("status", status);
            map.put("transactionId", resp.get("transaction_id"));
            map.put("remark", JSON.toJSONString(resp));
            map.put("finish_time", finishTime);
            int effectCount = rechargeRecordMapper.updatePayResult(map);
            if (effectCount > 0) {
                if (status == PayStatus.Success.getIndex()) {
                    //更新业务数据
                    RechargeProduct rechargeProduct = rechargeProductBiz.selectByPrice(rechargeRecord.getAmount());
                    if (rechargeProduct != null) {
                        String timeStr = rechargeProduct.getTime();
                        if (ValidateUtil.isAddTime(timeStr)) {
                            UserRechargeRecord userRechargeRecord = userRechargeRecordBiz.selectByOrderNumber(rechargeRecord.getOrderNumber());
                            if (userRechargeRecord != null) {
                                effectCount = userRechargeRecordBiz.updateStatusByOrderNumber(rechargeRecord.getOrderNumber(), status, finishTime);
                                if (effectCount > 0) {
                                    User user = userBiz.selectByPrimaryKey(userRechargeRecord.getUserId());
                                    if (user != null) {
                                        Date expireTime = user.getExpireTime();
                                        if (expireTime == null) {
                                            expireTime = new Date();
                                        }
                                        int time = NumberUtils.toInt(timeStr.substring(0, timeStr.length() - 1), 0);
                                        if (time > 0) {
                                            if (timeStr.endsWith("D")) {
                                                expireTime = DateUtils.addDays(expireTime, time);
                                            } else if (timeStr.endsWith("M")) {
                                                expireTime = DateUtils.addMonths(expireTime, time);
                                            } else if (timeStr.endsWith("Y")) {
                                                expireTime = DateUtils.addYears(expireTime, time);
                                            }
                                            effectCount = userBiz.updateExpireTime(user.getUserId(), expireTime);
                                            if (effectCount == 0) {
                                                throw new RuntimeException("更新用户到期时间失败");
                                            }
                                        } else {
                                            throw new RuntimeException("格式化时间错误");
                                        }
                                    } else {
                                        throw new RuntimeException("未找到用户");
                                    }
                                } else {
                                    throw new RuntimeException("更新用户流水失败");
                                }
                            } else {
                                throw new RuntimeException("未找到用户流水号");
                            }
                        } else {
                            throw new RuntimeException("产品格式不正确");
                        }
                    } else {
                        throw new RuntimeException("没有找到对应的产品");
                    }
                }
                paySucc = true;
            } else {
                throw new RuntimeException("更新支付状态失败");
            }
        } else {
            throw new RuntimeException("未找到支付流水号");
        }
        return paySucc;
    }
}
