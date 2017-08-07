package com.htche.crm.util.pay;

import com.alibaba.fastjson.JSON;
import com.htche.crm.domain.pay.WxAccessTokenResult;
import com.htche.crm.util.HttpUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jankie on 16/5/26.
 */
public class WeixinUtil {

    protected static final Logger logger = LoggerFactory.getLogger(WeixinUtil.class);

    //公众号相关参数，appid,appsecret,appkey,merchantid
    public static final String _AppIdPub = "wxf0263f5d7f90b3ba";
    public static final String _AppSecretPub = "07f6ba2ad1578f6e826e8c732e262afb";
    public static final String _AppKeyPub = "7dbd5e4058a20dfde60fc150bfd7ea81";
    public static final String _MerchantIdPub = "1391875302";
    public static final String _WxNotifyUrl = "https://api2.grtstar.cn/rest/userrecharge/wxnotify";
    private static final String _WxAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static final String _WxJSTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";
    public static final String _WxAuthAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * 获取微信请求token值，用于微信通知，获取openId,用户信息等等
     *
     * @return
     */
    public static String getWxAccessToken() {
        String accessToken = "";
        String url = String.format("%s&appid=%s&secret=%s"
                , _WxAccessTokenUrl, _AppIdPub, _AppSecretPub);
        String responseStr = HttpUtil.get(url);
        WxAccessTokenResult result = JSON.parseObject(responseStr, WxAccessTokenResult.class);
        if (result != null && StringUtils.isNotEmpty(result.getAccess_token())) {
            accessToken = result.getAccess_token();
        }
        return accessToken;
    }

    /**
     * 获取微信请求jsTicket值，用于微信分享
     *
     * @return
     */
    public static String getWxJSTicket() {
        String accessToken = getWxAccessToken();
        String jsTicket = "";
        String url = String.format("%s?access_token=%s&type=jsapi"
                , _WxJSTicketUrl, accessToken);
        String responseStr = HttpUtil.get(url);
        WxAccessTokenResult result = JSON.parseObject(responseStr, WxAccessTokenResult.class);
        if (result != null && StringUtils.isNotEmpty(result.getTicket())) {
            jsTicket = result.getTicket();
        }
        logger.info("accessToken:{},jsTicket:{}", accessToken, jsTicket);
        return jsTicket;
    }

    /**
     * 生成分享签名
     *
     * @param map
     * @return
     */
    public static String wxShareSign(Map<String, Object> map) {
        List<String> keys = new ArrayList<>();
        for (String key : map.keySet()) {
            keys.add(key);
        }
        //把参数按照ASCII
        Collections.sort(keys, String.CASE_INSENSITIVE_ORDER);
        //拼接参数
        String param = "";
        int i = 0;
        for (String key : keys) {
            Object value = map.get(key);
            if (StringUtils.isNotBlank(value.toString())) {
                param += String.format("%s%s=%s", i == 0 ? "" : "&", key, value);
                i++;
            }
        }
        //生成签名数据
        String sign = DigestUtils.sha1Hex(param).toLowerCase();
        return sign;
    }
}
