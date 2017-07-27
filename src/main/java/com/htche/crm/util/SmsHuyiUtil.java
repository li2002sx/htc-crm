package com.htche.crm.util;

import com.htche.crm.domain.AdminUser;
import com.htche.crm.model.SmsHuyiResult;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.IOException;

/**
 * Created by jankie on 2017/5/22.
 */
public class SmsHuyiUtil {

    private static String _HuYiUrl = "http://106.ihuyi.cn/webservice/sms.php?method=Submit";

    public static SmsHuyiResult sendSms(String mobile, String content, String appId, String appKey) {

        SmsHuyiResult smsHuyiResult = new SmsHuyiResult();

        try {
            NameValuePair[] data = {//提交短信
                    new NameValuePair("account", appId), //查看用户名请登录用户中心->验证码、通知短信->帐户及签名设置->APIID
                    new NameValuePair("password", appKey),  //查看密码请登录用户中心->验证码、通知短信->帐户及签名设置->APIKEY
                    //new NameValuePair("password", util.StringUtil.MD5Encode("密码")),
                    new NameValuePair("mobile", mobile),
                    new NameValuePair("content", content),
            };

            String responseStr = HttpUtil.post(_HuYiUrl, data);
            Document doc = DocumentHelper.parseText(responseStr);
            Element root = doc.getRootElement();

            String code = root.elementText("code");
            String msg = root.elementText("msg");
            String smsid = root.elementText("smsid");

            smsHuyiResult.setCode(code);
            smsHuyiResult.setMsg(msg);
            smsHuyiResult.setSmsId(smsid);

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return smsHuyiResult;
    }
}
