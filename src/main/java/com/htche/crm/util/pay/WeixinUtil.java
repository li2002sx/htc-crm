package com.htche.crm.util.pay;

import com.alibaba.fastjson.JSON;
import com.htche.crm.domain.pay.WxAccessTokenResult;
import com.htche.crm.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

/**
 * Created by jankie on 16/5/26.
 */
public class WeixinUtil {

    //公众号相关参数，appid,appsecret,appkey,merchantid
    public static final String _AppIdPub = "wxf0263f5d7f90b3ba";
    private static final String _AppSecretPub = "07f6ba2ad1578f6e826e8c732e262afb";
    public static final String _AppKeyPub = "7dbd5e4058a20dfde60fc150bfd7ea81";
    public static final String _MerchantIdPub = "1391875302";
    public static final String _WxNotifyUrl = "http://172.168.1.26:8081/rest/userrecharge/wxnotify";
    private static final String _WxAccessTokenUrl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential";
    private static final String _WxJSTicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

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
        if (result != null && StringUtils.isNotEmpty(result.getAccess_token())) {
            jsTicket = result.getTicket();
        }
        return jsTicket;
    }
}
