package com.htche.crm.util;

import com.alibaba.fastjson.JSON;
import com.htche.crm.domain.pay.WxAccessTokenResult;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * TODO: just quick workaround, to enhance soon
 */
public class HttpUtil {

    /**
     * http get请求
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(url);
        client.getParams().setContentCharset("utf-8");
        try {
            client.executeMethod(method);
            String responseStr = method.getResponseBodyAsString();
            return responseStr;
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * http post
     *
     * @param url
     * @param data
     * @return
     */
    public static String post(String url, NameValuePair[] data) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(url);

        client.getParams().setContentCharset("utf-8");
        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=utf-8");

        method.setRequestBody(data);

        try {
            client.executeMethod(method);
            String responseStr = method.getResponseBodyAsString();
            return responseStr;
        } catch (HttpException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取真实的IP地址
     *
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = ip.indexOf(",");
            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
            return ip;
        }
        return request.getRemoteAddr();
    }

    /**
     * 读取流信息
     * @param is
     * @param charset
     * @return
     */
    public static String getContent(InputStream is, String charset) {
        String pageString = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            isr = new InputStreamReader(is, charset);
            br = new BufferedReader(isr);
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            pageString = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null){
                    is.close();
                }
                if(isr!=null){
                    isr.close();
                }
                if(br!=null){
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            sb = null;
        }
        return pageString;
    }
}
