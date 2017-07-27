package com.htche.crm.util.pay;

import com.github.wxpay.sdk.WXPayConfig;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.*;

/**
 * Created by lishengxi on 2017/7/20.
 */
public class WeixinPayConfig implements WXPayConfig {

    private byte[] certData;

    public WeixinPayConfig() {
//        try {
//            String certPath = "/Users/xiaoliguo/Downloads/cert2/apiclient_cert.p12";
//            File file = new File(certPath);
//            InputStream certStream = new FileInputStream(file);
//            this.certData = new byte[(int) file.length()];
//            certStream.read(this.certData);
//            certStream.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public String getAppID() {
        return WeixinUtil._AppIdPub;
    }

    public String getMchID() {
        return WeixinUtil._MerchantIdPub;
    }

    public String getKey() {
        return WeixinUtil._AppKeyPub;
    }

    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    public int getHttpReadTimeoutMs() {
        return 10000;
    }
}
